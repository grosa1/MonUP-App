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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import giovanni.tradingtoolkit.data.model.coin_response.Coin;
import giovanni.tradingtoolkit.data.model.coin_response.ResponseData;
import giovanni.tradingtoolkit.home_widget.CoinListWidget;
import giovanni.tradingtoolkit.main.SharedPrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadCoinService extends Service {

    public static final String LIST_LIMIT = "200";

    public static String currency;
    private List<Coin> coins;
    private CoinMarketCapService coinDataService;
    private Context context;
    public LoadCoinService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
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
        this.stopSelf();
        super.onDestroy();
    }

    public void loadCoinList(String currencyType, String limit) throws IOException {
        this.coinDataService.getList(currencyType, limit).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.isSuccessful()) {
                    ResponseData body = response.body();
                    if (null != body) {
                        coins = body.getData();
                        storeCache(coins);
                        CoinListWidget.updateWidget(context);
                    }
                } else {
                    restoreCache();
                    int statusCode = response.code();
                    Log.e("ERROR_CODE", String.valueOf(statusCode));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                restoreCache();
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }

    private void restoreCache() {
        String serialCoins = SharedPrefs.restoreString(getApplicationContext(), SharedPrefs.KEY_COINS_CACHE); //TODO controllare getApplicationContext
        if (!serialCoins.isEmpty()) {
            Type listType = new TypeToken<ArrayList<Coin>>() {
            }.getType();
            coins = (new Gson()).fromJson(serialCoins, listType);
        }
    }

    private void storeCache(final List<Coin> updatedCoins) {
        String serialCoins = (new Gson()).toJson(updatedCoins);
        SharedPrefs.storeString(getApplicationContext(), SharedPrefs.KEY_COINS_CACHE, serialCoins);
    }
}
