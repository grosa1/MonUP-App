package giovanni.tradingtoolkit.data.remote;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by giiio on 11/12/2017.
 */

public interface CryptoCompareApiService {

    //EXAMPLE URL https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,BCH,BTG,ETH,ETC,XMR,ZEC,DASH,LTC,NXT,BTCD,XRP,PPC,DODGE&tsyms=EUR,USD,BTC
    @GET("/data/pricemultifull")
    Call<JsonObject> getPricesList(@Query("fsyms") String coinType, @Query("tsyms") String priceType);

    @GET("/data/all/coinlist")
    Call<JsonObject> getCurrenciesList();
}

