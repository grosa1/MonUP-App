package giovanni.tradingtoolkit.news;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.remote.LoadCoinReceiver;
import giovanni.tradingtoolkit.data.remote.LoadNewsReceiver;
import giovanni.tradingtoolkit.data.remote.LoadNewsService;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import giovanni.tradingtoolkit.marketprices.CoinsListAdapter;
import io.cryptocontrol.cryptonewsapi.models.Article;

public class NewsFragment extends Fragment {

    @BindView(R.id.news_list)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_news)
    Button btnRefresh;
    @BindView(R.id.text_news)
    TextView textNews;

    private Context context;
    private List<Article> news;
    private NewsListAdapter listAdapter;

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.context = getContext();
        super.onCreate(savedInstanceState);

        //ArrayAdapter<Article> newsAdapter = new NewsAdapter();

        //listView.setAdapter(R.id.news_list, intent);
        listAdapter = new NewsListAdapter(getContext(), news, this::showArticleInfo);

        runService(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //API CALL
        runService(context);

        restoreCache();

        Log.e("NEWSFRAGMENT", "onCreateView: " + news );

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        context = this.getContext();

        btnRefresh.setOnClickListener(v -> {
            Log.e("API-RES", "Refresh");
            runService(context);
        });

        listAdapter.updateNewsList(news);
        Log.e("NEWSSSSS", "updateNewsList" + news);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

        return view;
    }

    private void showValuesError() {
        ToastManager.create(getContext(), getResources().getString(R.string.calculator_error));
    }

    private void showArticleInfo(Article article) {

        AlertDialog.Builder builder1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            builder1 = new AlertDialog.Builder(getContext());
        }
        if (builder1 != null) {
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.article_info, null);
            builder1.setView(dialogView);
            //builder1.setMessage(article.getDescription());
            builder1.setCancelable(true);
            TextView description = dialogView.findViewById(R.id.article_description);
            description.setText(article.getDescription());

//            Log.e("ARTICLE", "getThumbnail: " + article.getThumbnail());
//            Log.e("ARTICLE", "getDescription: " + article.getDescription());
//            Log.e("ARTICLE", "getId: " + article.getId());
//            Log.e("ARTICLE", "getOriginalImageUrl: " + article.getOriginalImageUrl());
//            Log.e("ARTICLE", "getPrimaryCategory: " + article.getPrimaryCategory());
//            Log.e("ARTICLE", "getPublishedAt: " + article.getPublishedAt());
//            Log.e("ARTICLE", "getSentiment: " + article.getSentiment());
//            Log.e("ARTICLE", "getSourceDomain: " + article.getSourceDomain());
//            Log.e("ARTICLE", "getSource: " + article.getSource());
//            Log.e("ARTICLE", "getTitle: " + article.getTitle());
//            Log.e("ARTICLE", "getUrl: " + article.getUrl());
//            Log.e("ARTICLE", "getActivityHotness: " + article.getActivityHotness());
//            Log.e("ARTICLE", "getCoins: " + article.getCoins());
//            Log.e("ARTICLE", "getHotness: " + article.getHotness());
//            Log.e("ARTICLE", "getSimilarArticles: " + article.getSimilarArticles());





            builder1.setPositiveButton(
                    R.string.article_url,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            showInBrowser(article.getUrl());
                        }
                    });
//
//            builder1.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
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

    public void runService(Context context) {
        Intent i = new Intent(context, LoadNewsReceiver.class);
        context.sendBroadcast(i);
        listAdapter.notifyDataSetChanged();
        restoreCache();
    }

    private void restoreCache() {
        String serialNews = SharedPrefs.restoreString(context, SharedPrefs.KEY_NEWS_CACHE); //TODO controllare getApplicationContext
        if (!serialNews.isEmpty()) {
            Type listType = new TypeToken<ArrayList<Article>>() {
            }.getType();
            news = (new Gson()).fromJson(serialNews, listType);
            Log.e("RESTORECACHE", "restoreCache: " + news );
        }
    }
}
