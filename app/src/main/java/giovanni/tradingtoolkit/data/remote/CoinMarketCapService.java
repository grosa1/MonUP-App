package giovanni.tradingtoolkit.data.remote;

import giovanni.tradingtoolkit.data.model.ResponseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
/**
 * Created by giiio on 21/12/2017.
 */

public interface CoinMarketCapService {
    @Headers({
            "X-CMC_PRO_API_KEY: 246f39bb-985c-4022-8114-699e5c85f508",
            "Content-Type: application/json; charset=utf-8"
    })

    @GET("/v1/cryptocurrency/listings/latest")
    Call<ResponseData> getList(@Query("convert") String currency, @Query("limit") String listLimit);
}
