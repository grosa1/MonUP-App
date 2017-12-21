package giovanni.tradingtoolkit.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giiio on 11/12/2017.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) { //SET BASE URL FOR RETROFIT INSTANCE
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())  //USE GSON AD JSON CONVERTER
                    .build();
        }
        return retrofit;
    }

    public static final String BASE_URL = "https://api.coinmarketcap.com/";

    public static ApiService getRestService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
