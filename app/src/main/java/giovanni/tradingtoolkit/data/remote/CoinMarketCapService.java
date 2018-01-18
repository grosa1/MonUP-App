package giovanni.tradingtoolkit.data.remote;

import com.google.gson.JsonObject;

import java.util.List;

import giovanni.tradingtoolkit.data.model.Coin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by giiio on 21/12/2017.
 */

public interface CoinMarketCapService {

    @GET("/v1/ticker/")
    Call<List<Coin>> getList(@Query("convert") String currency, @Query("limit") String listLimit);
}
