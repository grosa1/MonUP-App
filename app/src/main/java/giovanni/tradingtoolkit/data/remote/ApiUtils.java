package giovanni.tradingtoolkit.data.remote;

/**
 * Created by giiio on 11/12/2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://min-api.cryptocompare.com";

    public static CryptoCompareApiService getRestService() {
        return RetrofitClient.getClient(BASE_URL).create(CryptoCompareApiService.class);
    }
}
