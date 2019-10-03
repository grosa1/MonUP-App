package giovanni.tradingtoolkit.data.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.data.model.Variation;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import giovanni.tradingtoolkit.marketprices.CoinsFragment;
import giovanni.tradingtoolkit.marketprices.CoinsListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadCoinService extends Service {

    public static final String ARG_CHART_SYM = "price_data";
    public static final String ARG_CHART_CURRENCY = "currency_data";
    public static final String DEFAULT_CURRENCY = "USD";
    public static final String LIST_LIMIT = "200";

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout pullDown;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.tv_coin_name)
    TextView tvCoin;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.spinner_currency)
    Spinner spinnerCurrency;
    @BindView(R.id.tv_percentage_variation)
    TextView tvVariation;

    public static String currency;
    private List<Coin> coins;
    private CoinsListAdapter listAdapter;
    private CoinMarketCapService coinDataService;
    private boolean isConnected = false;

    private Variation currentVariation;
    private boolean sortPriceAsc, sortNameAsc, sortRankAsc;

//    public CoinsFragment() {
//        coins = new ArrayList<>();
//        currentVariation = Variation.Daily;
//    }

    public static CoinsFragment newInstance() {
        CoinsFragment fragment = new CoinsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public LoadCoinService() {

        Toast.makeText(this, "Invoke background service LoadCoinServiceStart.", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();
    }


    public void loadCoinList(String currencyType, String limit) throws IOException {
        this.coinDataService.getList(currencyType, limit).enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {

                Log.d("RES", response.body().toString());

                if (response.isSuccessful()) {
                    List<Coin> body = response.body();
                    if (null != body) {
                        isConnected = true;
                        coins = body;
                        storeCache(coins);
                        listAdapter.updateCoinsList(coins);
                    }

                    ProgressDialogManager.close();

                    if (pullDown != null) {
                        pullDown.setRefreshing(false);
                    }

                } else {
                    isConnected = false;
                    restoreCache();
                    Log.d("ERR_RES", response.errorBody().toString());
                    ProgressDialogManager.close();
                    if (pullDown != null) {
                        pullDown.setRefreshing(false);
                    }

                    int statusCode = response.code();
                    Log.e("ERROR_CODE", String.valueOf(statusCode));
                    ToastManager.create(getApplicationContext(), getResources().getString(R.string.coins_request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
                isConnected = false;
                restoreCache();
                ProgressDialogManager.close();

                if (pullDown != null) {
                    pullDown.setRefreshing(false);
                }
                ToastManager.create(getApplicationContext(), getResources().getString(R.string.coins_request_error));
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }

    private boolean restoreCache() {
        String serialCoins = SharedPrefs.restoreString(getApplicationContext(), SharedPrefs.KEY_COINS_CACHE); //TODO controllare getApplicationContext
        if (!serialCoins.isEmpty()) {
            Type listType = new TypeToken<ArrayList<Coin>>() {
            }.getType();
            List<Coin> cached = (new Gson()).fromJson(serialCoins, listType);
            coins = cached;
            listAdapter.updateCoinsList(cached);
            return true;
        }
        return false;
    }

    private boolean storeCache(final List<Coin> updatedCoins) {
        String serialCoins = (new Gson()).toJson(updatedCoins);
        if (SharedPrefs.storeString(getApplicationContext(), SharedPrefs.KEY_COINS_CACHE, serialCoins)) {
            return true;
        }

        return false;
    }

}
