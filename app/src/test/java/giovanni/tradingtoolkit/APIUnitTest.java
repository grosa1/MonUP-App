package giovanni.tradingtoolkit;

import org.junit.Test;

import java.lang.reflect.Method;

import giovanni.tradingtoolkit.data.model.HystoPrice;
import giovanni.tradingtoolkit.data.model.HystoPriceResponse;
import giovanni.tradingtoolkit.data.model.coin_response.Coin;
import giovanni.tradingtoolkit.data.model.coin_response.ResponseData;
import giovanni.tradingtoolkit.data.remote.RetrofitClient;
import giovanni.tradingtoolkit.marketprices.CoinsFragment;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

public class APIUnitTest {
    @Test
    public void coinMarketCapAPITest() throws Exception {
        Response<ResponseData> response = RetrofitClient.getCoinMarketCapService().getList(CoinsFragment.DEFAULT_CURRENCY, CoinsFragment.LIST_LIMIT).execute();

        assertTrue(response.isSuccessful());

        assertTrue(response.body().getData().get(0) != null);
        Coin coin = response.body().getData().get(0);

        for (Method m : coin.getClass().getMethods()) {
            if (m.getName().startsWith("get"))
                assertTrue(m.invoke(coin) != null);
        }
    }

    @Test
    public void cryptoCompareAPITest() throws Exception {
        Response<HystoPriceResponse> response = RetrofitClient.getCryptoCompareService().getList("BTC", "USD", "90").execute();

        assertTrue(response.isSuccessful());

        assertTrue(response.body().getData().get(0) != null);
        HystoPrice hystoPrice = response.body().getData().get(0);

        for (Method m : hystoPrice.getClass().getMethods()) {
            if (m.getName().startsWith("get"))
                assertTrue(m.invoke(hystoPrice) != null);
        }
    }
}