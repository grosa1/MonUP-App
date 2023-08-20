package giovanni.tradingtoolkit.news.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApiClient {
    public static Retrofit getClient(String baseUrl) { //SET BASE URL FOR RETROFIT INSTANCE
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())  //USE GSON AS JSON CONVERTER
                .build();
    }

    public static NewsApiService getNewsApiService() {
        return NewsApiClient.getClient("https://gnews.io/api/v4/").create(NewsApiService.class);
    }
}
