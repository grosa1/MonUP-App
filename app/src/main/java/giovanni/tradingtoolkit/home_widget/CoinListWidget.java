package giovanni.tradingtoolkit.home_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import giovanni.tradingtoolkit.data.remote.LoadCoinReceiver;
import giovanni.tradingtoolkit.data.remote.LoadCoinService;
import giovanni.tradingtoolkit.main.ToastManager;
import giovanni.tradingtoolkit.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CoinListWidgetConfigureActivity CoinListWidgetConfigureActivity}
 */
public class CoinListWidget extends AppWidgetProvider {

    public static final String REFRESH_ON_CLICK = "REFRESH_ON_CLICK";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_coin_list);
        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REFRESH_ON_CLICK.equals(intent.getAction())) {
            runService(context);
            refresh(context);
            ToastManager.create(context, R.string.widget_refreshed);
        }
        super.onReceive(context, intent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static void refresh(Context context) {
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

    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, CoinListWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context.getApplicationContext()).getAppWidgetIds(new ComponentName(context.getApplicationContext(), CoinListWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
        refresh(context);
        stopService(context);
    }

    public static void runService(Context context) {
        Intent i = new Intent(context, LoadCoinReceiver.class);
        context.sendBroadcast(i);
    }

    public static void stopService(Context context) {
        Intent mServiceIntent;
        LoadCoinService mSensorService;
        mSensorService = new LoadCoinService();
        mServiceIntent = new Intent(context, mSensorService.getClass());
        context.stopService(mServiceIntent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


