package giovanni.tradingtoolkit.data.remote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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
import java.util.Objects;

import butterknife.BindView;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.home_widget.CoinListWidget;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadCoinService extends Service {

    public static final String LIST_LIMIT = "200";

    public static String currency;
    private List<Coin> coins;
    private CoinMarketCapService coinDataService;

    public LoadCoinService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();

        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) Objects.requireNonNull(getSystemService(Context.NOTIFICATION_SERVICE))).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currency = SharedPrefs.restoreString(getApplicationContext(), SharedPrefs.KEY_CURRENCY);

        this.coinDataService = RetrofitClient.getCoinMarketCapService();

        //API CALL
        try {
            this.loadCoinList(currency, LIST_LIMIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();

        super.onDestroy();

        Intent broadcastIntent = new Intent(LoadCoinService.this, LoadCoinReceiver.class);

        sendBroadcast(broadcastIntent);
    }


    public void loadCoinList(String currencyType, String limit) throws IOException {
        this.coinDataService.getList(currencyType, limit).enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {

                if (response.body() != null) { //TODO Rimuovere
                    Log.d("RES", response.body().toString());
                }

                if (response.isSuccessful()) {
                    List<Coin> body = response.body();
                    if (null != body) {
                        //isConnected = true;
                        coins = body;
                        storeCache(coins);
                        CoinListWidget.refresh(LoadCoinService.this);
                    }
                } else {
                    restoreCache();
                    int statusCode = response.code();
                    Log.e("ERROR_CODE", String.valueOf(statusCode));

                }
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
                //isConnected = false;
                restoreCache();
                // ToastManager.create(getApplicationContext(), getResources().getString(R.string.coins_request_error));
                Log.e("REQUEST_ERROR", t.toString());
            }
        });

        super.onDestroy();
    }

    private void restoreCache() {
        String serialCoins = SharedPrefs.restoreString(getApplicationContext(), SharedPrefs.KEY_COINS_CACHE); //TODO controllare getApplicationContext
        if (!serialCoins.isEmpty()) {
            Type listType = new TypeToken<ArrayList<Coin>>() {
            }.getType();
            coins = (new Gson()).fromJson(serialCoins, listType);
            //listAdapter.updateCoinsList(cached);
        }
    }

    private void storeCache(final List<Coin> updatedCoins) {
        String serialCoins = (new Gson()).toJson(updatedCoins);
        SharedPrefs.storeString(getApplicationContext(), SharedPrefs.KEY_COINS_CACHE, serialCoins);
    }

}
