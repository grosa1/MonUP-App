package giovanni.tradingtoolkit.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giiio on 11/12/2017.
 */

public class RetrofitClient {

    public static Retrofit getClient(String baseUrl) { //SET BASE URL FOR RETROFIT INSTANCE
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())  //USE GSON AS JSON CONVERTER
                .build();
    }

    public static CoinMarketCapService getCoinMarketCapService() {
        return RetrofitClient.getClient("https://pro-api.coinmarketcap.com/").create(CoinMarketCapService.class);
    }

    public static CryptoCompareService getCryptoCompareService() {
        return RetrofitClient.getClient("https://min-api.cryptocompare.com/").create(CryptoCompareService.class);
    }
}
