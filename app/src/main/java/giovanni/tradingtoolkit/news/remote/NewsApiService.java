package giovanni.tradingtoolkit.news.remote;

import giovanni.tradingtoolkit.news.remote.model.NewsResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("search")
    Call<NewsResponseModel> getLatestNews(@Query("q") String paramQuery,
                                          @Query("lang") String paramLang,
                                          @Query("country") String paramCountry,
                                          @Query("max") String paramMax,
                                          @Query("apikey") String paramApiKey);
}
