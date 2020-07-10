package giovanni.tradingtoolkit.data.remote;

import giovanni.tradingtoolkit.data.model.HystoPriceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Giovanni on 15/01/2018.
 */

//https://min-api.cryptocompare.com/data/histoday?aggregate=1&e=CCCAGG&extraParams=CryptoCompare&fsym=BTC&limit=30&tryConversion=false&tsym=EUR
//https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=EUR

public interface CryptoCompareService {

    @GET("/data/histoday")
    Call<HystoPriceResponse> getList(@Query("fsym") String crypto, @Query("tsym") String currency, @Query("limit") String daysCount);

}
