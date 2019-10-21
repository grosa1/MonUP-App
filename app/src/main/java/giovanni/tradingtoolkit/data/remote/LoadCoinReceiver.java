package giovanni.tradingtoolkit.data.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class LoadCoinReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.i(LoadCoinReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        //context.startService(new Intent(context, LoadCoinService.class));
        Intent i = new Intent(context, LoadCoinService.class);
        ContextCompat.startForegroundService(context, i);

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}