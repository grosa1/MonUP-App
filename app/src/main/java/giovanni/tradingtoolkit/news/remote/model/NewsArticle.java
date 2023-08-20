package giovanni.tradingtoolkit.news.remote.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//{'title': 'crescono Nord Africa e Medio Oriente', 'description': 'Il Cryptocurrency Report Index 2022 conferma una forte crescita per il mercato in Nord Africa e Medio Oriente', 'content': 'Chainalysis ha pubblicato un nuovo capitolo del Geography of Cryptocurrency Report Index 2022 che mette in evidenza alcuni trend del settore delle criptovalute, con un confronto tra i dati di luglio 2021 e quelli di giugno 2022. In questo periodo di ... [1130 chars]', 'url': 'https://techprincess.it/cryptocurrency-report-index-2022-nord-africa-e-medio-oriente/', 'image': 'https://techprincess.it/wp-content/uploads/2022/10/Cryptocurrency-Report-Index-2022-1024x682.jpg', 'publishedAt': '2022-10-23T17:55:52Z', 'source': {'name': 'Tech Princess', 'url': 'https://techprincess.it'}}
//        dict_keys(['title', 'description', 'content', 'url', 'image', 'publishedAt', 'source'])

public class NewsArticle {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("publishedAt")
    private Date publishedAt;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setPublishedAt(String publishedAt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            this.publishedAt = dateFormat.parse(publishedAt);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing error as needed
        }
    }

    public String getSourceSite() {
        return this.url.split("/")[2].replace("www.", "");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }
}