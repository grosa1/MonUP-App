package giovanni.tradingtoolkit.data.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import giovanni.tradingtoolkit.R;

public class LoadCoinReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            Intent i = new Intent(context, LoadCoinService.class);
            ContextCompat.startForegroundService(context, i);
        } else {
            Toast.makeText(context, R.string.not_connected, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
