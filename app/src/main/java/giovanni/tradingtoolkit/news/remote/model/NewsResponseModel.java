package giovanni.tradingtoolkit.news.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponseModel {
    @SerializedName("articles")
    private List<NewsArticle> articles;

    public List<NewsArticle> getArticles() {
        return articles;
    }
}
