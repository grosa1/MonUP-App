package giovanni.tradingtoolkit.news;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.main.ResourcesLoader;
import giovanni.tradingtoolkit.news.remote.model.NewsArticle;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    public static final int NEWS_TITLE_MAX_LENGTH = 18;
    public static final int NEWS_IMAGE_TARGET_WIDTH = 512;
    public static final int NEWS_IMAGE_TARGET_HEIGHT = 512;
    private List<NewsArticle> news;
    private final Context context;
    private ArticleItemListener itemListener;

    NewsListAdapter(Context context, List<NewsArticle> news, ArticleItemListener itemListener) {
        this.news = news;
        this.context = context;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view, this.itemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsArticle article = news.get(position);

        Picasso.get()
                .load(article.getImageUrl())
                .resize(NEWS_IMAGE_TARGET_WIDTH, NEWS_IMAGE_TARGET_HEIGHT)
                .centerCrop()
                .error(R.drawable.trading_toolkit)
                .placeholder(R.drawable.trading_toolkit)
                .into(holder.icon);

        String title = article.getTitle();
        String[] titleTrimmed = title.split(" ");
        if (titleTrimmed.length > NewsListAdapter.NEWS_TITLE_MAX_LENGTH) {
            titleTrimmed = Arrays.copyOfRange(titleTrimmed, 0, NewsListAdapter.NEWS_TITLE_MAX_LENGTH);
            title = TextUtils.join(" ", titleTrimmed) + "...";
        }
        holder.title.setText(title);

        String sourcePrefix = context.getResources().getString(R.string.news_source);
        holder.sourceSite.setText(String.format("%s %s", sourcePrefix, article.getSourceSite()));

        holder.date.setText(this.formatArticleDate(article.getPublishedAt()));

        int background;
        if (position % 2 == 0) {
            background = ResourcesLoader.getColorFromId(context, R.color.lightGreyMaterial);
        } else {
            background = ResourcesLoader.getColorFromId(context, R.color.materialWhite);
        }
        holder.layout.setBackgroundColor(background);

        int colorId;
        // Setting Text Color
        colorId = R.color.textColorSecondary;
        holder.title.setTextColor(context.getResources().getColor(colorId));
        holder.date.setTextColor(context.getResources().getColor(colorId));
    }

    @Override
    public int getItemCount() {
        if (news != null && !news.isEmpty()){
            return news.size();
        }
        return 0;
    }

    void updateNewsList(List<NewsArticle> items) {
        news = items;
        notifyDataSetChanged();
    }

    private NewsArticle getItem(int adapterPosition) {
        return news.get(adapterPosition);
    }

    public interface ArticleItemListener {
        void onArticleClick(NewsArticle article);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.article_icon)
        public ImageView icon;
        @BindView(R.id.article_title)
        public TextView title;
        @BindView(R.id.article_date)
        public TextView date;
        @BindView(R.id.article_source)
        public TextView sourceSite;
        @BindView(R.id.news_list_item)
        public LinearLayout layout;

        ViewHolder(View itemView, ArticleItemListener articleItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemListener = articleItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            NewsArticle article = getItem(getAdapterPosition());
            itemListener.onArticleClick(article);
        }
    }

    private String formatArticleDate(Date articleDate) {
        String dateText = "";

        try {
            assert articleDate != null;
            dateText = DateFormat.getDateInstance(DateFormat.MEDIUM).format(articleDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateText;
    }

    //TODO: Implement filters
//    public void filterList(ArrayList<Article> filteredList) {
//        news = filteredList;
//        notifyDataSetChanged();
//    }
}




