package giovanni.tradingtoolkit.marketprices.remote;

import giovanni.tradingtoolkit.BuildConfig;
import giovanni.tradingtoolkit.marketprices.remote.model.coin_response.ResponseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
/**
 * Created by giiio on 21/12/2017.
 */

// https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=USD&limit=200

public interface CoinMarketCapService {
    @Headers({
            "X-CMC_PRO_API_KEY:" + BuildConfig.COINMARKETCAP_API_KEY,
            "Content-Type: application/json; charset=utf-8"
    })

    @GET("/v1/cryptocurrency/listings/latest")
    Call<ResponseData> getList(@Query("convert") String currency, @Query("limit") String listLimit);
}