package giovanni.tradingtoolkit.home_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.SharedPrefs;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link personalizableCoinListWidgetConfigureActivity personalizableCoinListWidgetConfigureActivity}
 */
public class personalizable_coin_list_widget extends AppWidgetProvider {

    private Context mContext;
    private List<Coin> coins;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //  CharSequence widgetText = personalizableCoinListWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.personalizable_coin_list_widget);
       //  views.setTextViewText(R.id.appwidget_text, widgetText);

//        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coins, itemListener);
//        coinsListAdapter.filterList(filteredList);
//        recyclerView.setAdapter(coinsListAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //ListView coinsList =

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        // for (int appWidgetId : appWidgetIds) {
        //    personalizableCoinListWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        //}
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void restorePreferences(Context context) {
        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_WIDGET);

        if( !storedPreferences.isEmpty()) {

            Type listType = new TypeToken<ArrayList<Coin>>() {
            }.getType();
            coins = (new Gson()).fromJson(storedPreferences, listType);

            Log.e("PREFERENCES", storedPreferences);
            Log.e("PREFERENCES-COIN", coins.toString());

        }

        Log.e("PREFERENCES", "Coins Restored");
    }
}

