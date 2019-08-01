package giovanni.tradingtoolkit.home_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import giovanni.tradingtoolkit.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CoinListWidgetConfigureActivity CoinListWidgetConfigureActivity}
 */
public class CoinListWidget extends AppWidgetProvider {

    private static final String REFRESH_ON_CLICK = "refreshOnClickTag";

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
        }

        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_coin_list);
        watchWidget = new ComponentName(context, CoinListWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.refresh, getPendingSelfIntent(context, REFRESH_ON_CLICK));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (REFRESH_ON_CLICK.equals(intent.getAction())) {

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
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
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


