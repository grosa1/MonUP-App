package giovanni.tradingtoolkit.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.main.ResourcesLoader;
import io.cryptocontrol.cryptonewsapi.models.Article;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<Article> news;
    private Context context;
    private ArticleItemListener itemListener;

    NewsListAdapter(Context context, List<Article> news, ArticleItemListener itemListener) {
        this.news = news;
        this.context = context;
        this.itemListener = itemListener;
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsListAdapter.ViewHolder(view, this.itemListener);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ViewHolder holder, int position) {
        Article article = news.get(position);

        Picasso.get().load(article.getThumbnail()).into(holder.icon);
        holder.title.setText(article.getTitle());
        holder.date.setText(this.formatArticleDate(article.getPublishedAt()));

        int background;
        if (position % 2 == 0) {
            background = ResourcesLoader.getColorFromId(context, R.color.lightGreyMaterial);
        } else {
            background = ResourcesLoader.getColorFromId(context, R.color.materialWhite);
        }
        holder.layout.setBackgroundColor(background);
    }

    @Override
    public int getItemCount() {
        if (news != null && !news.isEmpty()){
            return news.size();
        }
        return 0;
    }

    void updateNewsList(List<Article> items) {
        news = items;
        notifyDataSetChanged();
    }

    private Article getItem(int adapterPosition) {
        return news.get(adapterPosition);
    }

    public interface ArticleItemListener {
        void onArticleClick(Article article);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.article_icon)
        public ImageView icon;
        @BindView(R.id.article_title)
        public TextView title;
        @BindView(R.id.article_date)
        public TextView date;
        @BindView(R.id.news_list_item)
        public LinearLayout layout;

        ViewHolder(View itemView, NewsListAdapter.ArticleItemListener articleItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemListener = articleItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Article article = getItem(getAdapterPosition());
            itemListener.onArticleClick(article);
        }
    }

    private String formatArticleDate(String articleDate) {
        String dateText = "";

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date artDate = format.parse(articleDate);
            dateText = DateFormat.getDateInstance(DateFormat.MEDIUM).format(artDate);
        } catch (ParseException e) {
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




