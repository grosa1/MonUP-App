package giovanni.tradingtoolkit.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.BuildConfig;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import giovanni.tradingtoolkit.news.remote.NewsApiClient;
import giovanni.tradingtoolkit.news.remote.NewsApiService;
import giovanni.tradingtoolkit.news.remote.model.NewsArticle;
import giovanni.tradingtoolkit.news.remote.model.NewsResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    public static final int NEWS_UPDATE_INTERVAL = 60 * 60 * 24;  // 24 h
    public static final String NEWS_TOPIC_QUERY = "cryptocurrency";
    public static final int NEWS_COUNT = 10;

    @BindView(R.id.news_list)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_news)
    Button btnRefresh;
    @BindView(R.id.text_news)
    TextView textNews;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout pullDown;

    private NewsApiService newsApiService;
    private Context context;
    private List<NewsArticle> news;
    private NewsListAdapter listAdapter;

    private String newsCountry;
    private String newsLang;

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.newsCountry = Locale.getDefault().getLanguage();
        if (this.newsCountry.startsWith("it")) {
            this.newsCountry = "it";
            this.newsLang = "it";
        } else {
            this.newsCountry = "us";
            this.newsLang = "en";
        }

        this.context = getContext();
        super.onCreate(savedInstanceState);
        listAdapter = new NewsListAdapter(getContext(), news, this::showArticleInfo);

        // Initialize the NewsApiService using the ApiClient
        this.newsApiService = NewsApiClient.getNewsApiService();

        fetchNews();
//        runServ¶ice(context);
    }

    void fetchNews() {
        long lastUpdate = 0;
        try {
            lastUpdate = Long.parseLong(SharedPrefs.restoreString(Objects.requireNonNull(getContext()), SharedPrefs.KEY_NEWS_LAST_UPDATE));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (lastUpdate > 0) {
            if ((new Date().getTime() - lastUpdate) > NEWS_UPDATE_INTERVAL) {
                Log.i("NEWS", "Refreshing news");
                try {
                    loadNewsList(this.newsCountry, this.newsLang);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("NEWS", "News update interval not reached");
            }
        } else {
            Log.i("NEWS", "Refreshing news");
            try {
                loadNewsList(this.newsCountry, this.newsLang);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //runService(context);
        restoreCache();

        View view = inflater.inflate(R.layout.news_list_fragment, container, false);
        ButterKnife.bind(this, view);
        context = this.getContext();

        btnRefresh.setOnClickListener(v -> {
            ProgressDialogManager.open(getContext());
            refresh();
            ProgressDialogManager.close();
            ToastManager.create(context, R.string.news_refreshed);
        });

        // SET PULL DOWN LISTENER
        pullDown.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // This method will be called when the user swipes down to refresh
                // Place your refresh logic here

                // Once the refreshing process is complete, call setRefreshing(false)
                // to stop the loading animation
                // For example:
                // swipeRefreshLayout.setRefreshing(false);
                refresh();
                ToastManager.create(context, R.string.news_refreshed);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        listAdapter.updateNewsList(news);

        return view;
    }

    private void showArticleInfo(NewsArticle article) {
        showInBrowser(article.getUrl());
    }

    public void showInBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Here we use an intent without a Chooser unlike the next example
            startActivity(intent);
        }
    }

    private void refresh() {
//        runService(context); TODO: fix service for new APIs
        fetchNews();

        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        listAdapter.notifyDataSetChanged();
    }

    private boolean restoreCache() {
        String serialNews = SharedPrefs.restoreString(Objects.requireNonNull(getContext()), SharedPrefs.KEY_NEWS_CACHE);
        if (!serialNews.isEmpty()) {
            Type listType = new TypeToken<ArrayList<NewsArticle>>() {
            }.getType();
            List<NewsArticle> cached = (new Gson()).fromJson(serialNews, listType);
            news = cached;
            listAdapter.updateNewsList(cached);
            return true;
        }
        return false;
    }

    private void storeCache(final List<NewsArticle> updatedNews) {
        String serialCoins = (new Gson()).toJson(updatedNews);
        SharedPrefs.storeString(Objects.requireNonNull(getContext()), SharedPrefs.KEY_NEWS_CACHE, serialCoins);
    }

    public void loadNewsList(String newsCountry, String newsLang) throws IOException {
        // Show progress dialog
        ProgressDialogManager.open(getContext());

        newsApiService.getLatestNews(
                NEWS_TOPIC_QUERY,
                newsLang,
                newsCountry,
                String.valueOf(NEWS_COUNT),
                BuildConfig.NEWS_API_KEY
        ).enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponseModel> call, @NonNull Response<NewsResponseModel> response) {

                if (response.body() != null) {
                    Log.d("RES", response.body().toString());
                }

                if (response.isSuccessful()) {
                    NewsResponseModel body = response.body();
                    if (null != body) {
                        news = body.getArticles();
                        storeCache(news);
                        listAdapter.updateNewsList(news);
                        SharedPrefs.storeString(Objects.requireNonNull(getContext()), SharedPrefs.KEY_NEWS_LAST_UPDATE, String.valueOf(new Date().getTime()));
                    }

                    // Close progress dialog
                    ProgressDialogManager.close();

                    if (pullDown != null) {
                        pullDown.setRefreshing(false);
                    }

                } else {
//                    isConnected = false;
                    boolean isCache = restoreCache();

                    int statusCode = response.code();
                    Log.e("ERROR_CODE", String.valueOf(statusCode));
                    assert response.errorBody() != null;
                    Log.d("ERR_RES", response.errorBody().toString());

                    // Close progress dialog
                    ProgressDialogManager.close();
                    if (pullDown != null) {
                        pullDown.setRefreshing(false);
                    }

                    if (isCache) {
                        ToastManager.create(getContext(), "Unable to fetch the latest News list, please try again later");
                    } else {
                        ToastManager.makeAlert(getContext(), "Error", "Unable to fetch the latest News list, please try again later");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponseModel> call, @NonNull Throwable t) {
//                isConnected = false;
                boolean isCache = restoreCache();

                // Close progress dialog
                ProgressDialogManager.close();

                if (pullDown != null) {
                    pullDown.setRefreshing(false);
                }

                if (isCache) {
                    ToastManager.create(getContext(), "Unable to fetch the latest News list, please try again later");
                } else {
                    ToastManager.makeAlert(getContext(), "Error", "Unable to fetch the latest News list, please try again later");
                }
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }


}
