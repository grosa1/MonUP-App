package giovanni.tradingtoolkit.home_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.remote.LoadCoinReceiver;
import giovanni.tradingtoolkit.main.MainActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CoinListWidgetConfigureActivity CoinListWidgetConfigureActivity}
 */
public class CoinListWidget extends AppWidgetProvider {

    public static final String REFRESH_ON_CLICK = "refreshOnClickTag";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_coin_list);
        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        //refresh(context);
        Log.e("UPDATEAPPWID", "UPDA");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.e("ONUPDATE", "UPDA");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_coin_list);
            watchWidget = new ComponentName(context, CoinListWidget.class);

            remoteViews.setOnClickPendingIntent(R.id.refresh, getPendingSelfIntent(context, REFRESH_ON_CLICK));
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
            //refresh(context);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ONRECEIVE", "UPDA");

        super.onReceive(context, intent);

        if (REFRESH_ON_CLICK.equals(intent.getAction())) {
            Intent broadcastIntent = new Intent(context, LoadCoinReceiver.class);
            context.sendBroadcast(broadcastIntent);
            //refresh(context);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static void refresh(Context context) {
        Log.e("REFRESH", "UPDA");

        Toast.makeText(context, R.string.widget_refreshed, Toast.LENGTH_SHORT).show();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName watchWidget;
        watchWidget = new ComponentName(context, CoinListWidget.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_coin_list);
        Intent i = new Intent(context, WidgetService.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CoinListWidget.class));

        views.setRemoteAdapter(R.id.widget_list, i);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_list);
        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    @Override
    public void onEnabled(Context context) {
        Log.e("ONENABLE", "UPDA");

        //this.refresh(context);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


