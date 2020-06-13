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

import java.util.List;
import java.util.Objects;

import giovanni.tradingtoolkit.main.SharedPrefs;
import io.cryptocontrol.cryptonewsapi.CryptoControlApi;
import io.cryptocontrol.cryptonewsapi.models.Article;

public class LoadNewsService extends Service {

    private static final String API_KEY = "d97b2bdfeb879304ab5fbbbbba5cd87b";
    private static final String TAG = "LoadNewsService";

    public LoadNewsService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
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

        new Thread(new Thread() {

            @Override
            public void run() {

                // Connect to the CryptoControl API
                CryptoControlApi api = new CryptoControlApi(API_KEY);

                // Get top crypto news
                api.getTopNews(new CryptoControlApi.OnResponseHandler<List<Article>>() {
                    public void onSuccess(List<Article> news) {
//                        for (Article article : news) {
//                            Log.e("API-RESPONSE", "onSuccess News: " + article.getTitle());
//                        }
                        Log.d(TAG, "onSuccess News: " + news);
                        storeCache(news);
                        onDestroy();
                    }

                    public void onFailure(Exception e) {
                        Log.e(TAG, "Failed " + e);
                        e.printStackTrace();
                    }
                });


                //TODO: Not yet implemented Apis
/*               // Get latest tweets for bitcoin
                api.getLatestTweetsByCoin("bitcoin", new CryptoControlApi.OnResponseHandler<List<Tweet>>() {
                    @Override
                    public void onSuccess(List<Tweet> body) {
                        for (Tweet post : body) {
                            Log.e("API-RES", "onSuccess: " + post.getId());
                        }
                    }


                    @Override
                    public void onFailure(Exception e) {
                        Log.e("API-RES", "Failed " + e);
                        e.printStackTrace();
                    }
                });

                // Get latest russian crypto news
                api.getLatestNews(Language.ENGLISH, new CryptoControlApi.OnResponseHandler<List<Article>>() {
                    @Override
                    public void onSuccess(List<Article> body) {
                        for (Article article : body) {
                            Log.e("API-RES", "onSuccess: " + article.getTitle());
                        }
                    }


                    @Override
                    public void onFailure(Exception e) {
                        Log.e("API-RES", "Failed " + e);
                        e.printStackTrace();
                    }
                });

                // Get rich metadata (wallets, blockexplorers, twitter handles etc..) for ethereum
                api.getCoinDetails("ethereum", new CryptoControlApi.OnResponseHandler<CoinDetail>() {
                    @Override
                    public void onSuccess(CoinDetail body) {
                        Log.e("API-RES", "onSuccess: " + body.getDescription());
                    }


                    @Override
                    public void onFailure(Exception e) {
                        Log.e("API-RES", "Failed " + e);
                        e.printStackTrace();
                    }
                 });
 */

            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        this.stopSelf();
        super.onDestroy();
    }

    private void storeCache(final List<Article> updatedNews) {
        String serialNews = (new Gson()).toJson(updatedNews);
        SharedPrefs.storeString(getApplicationContext(), SharedPrefs.KEY_NEWS_CACHE, serialNews);
    }
}
