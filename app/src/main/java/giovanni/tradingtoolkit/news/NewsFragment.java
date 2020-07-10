package giovanni.tradingtoolkit.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.data.remote.LoadNewsReceiver;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import io.cryptocontrol.cryptonewsapi.models.Article;

public class NewsFragment extends Fragment {

    @BindView(R.id.news_list)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_news)
    Button btnRefresh;
    @BindView(R.id.text_news)
    TextView textNews;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout pullDown;

    private Context context;
    private List<Article> news;
    private NewsListAdapter listAdapter;

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.context = getContext();
        super.onCreate(savedInstanceState);
        listAdapter = new NewsListAdapter(getContext(), news, this::showArticleInfo);
        runService(context);
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
            ToastManager.create(context, R.string.news_refreshed);
            refresh();
        });

        //SET PULL DOWN LISTENER
        pullDown.setOnRefreshListener(this::refresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        listAdapter.updateNewsList(news);

        return view;
    }

    private void showArticleInfo(Article article) {

        AlertDialog.Builder builder1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            builder1 = new AlertDialog.Builder(getContext());
        }
        if (builder1 != null) {
            View dialogView = View.inflate(context, R.layout.article_info, null);
            builder1.setView(dialogView);
            builder1.setCancelable(true);
            TextView description = dialogView.findViewById(R.id.article_description);
            description.setText(article.getDescription());

            builder1.setPositiveButton(
                    R.string.article_url, (dialog, id) -> {
                        dialog.cancel();
                        showInBrowser(article.getUrl());
                    });

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
        restoreCache();
    }

    private void refresh() {
        runService(context);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        listAdapter.notifyDataSetChanged();
    }

    private void restoreCache() {
        String serialNews = SharedPrefs.restoreString(context, SharedPrefs.KEY_NEWS_CACHE); //TODO controllare getApplicationContext
        if (!serialNews.isEmpty()) {
            Type listType = new TypeToken<ArrayList<Article>>() {
            }.getType();
            news = (new Gson()).fromJson(serialNews, listType);
        }
    }
}
