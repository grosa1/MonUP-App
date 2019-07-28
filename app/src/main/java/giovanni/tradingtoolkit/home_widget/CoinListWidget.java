package giovanni.tradingtoolkit.home_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.marketprices.CoinsListAdapter;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CoinListWidgetConfigureActivity CoinListWidgetConfigureActivity}
 */
public class CoinListWidget extends AppWidgetProvider {

    private ArrayList<String> coinsToObserve;
    private CoinsListAdapter.CoinItemListener itemListener;
    private ListView listView;
    private List<Coin> coins;
    private ArrayList<Coin> coinsToShow;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //  CharSequence widgetText = CoinListWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.personalizable_coin_list_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        //
        Intent intent = new Intent(context, WidgetService.class);
        //
        // intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
        //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

//        adapter = new CoinsListAdapter(context, coinsToShow, coinSym -> {
//            if (!(currency.equals("BTC") && coinSym.equals("BTC"))) {
//                this.showChart(coinSym, currency);
//            }
//        });
        //Intent intent = new Intent(context, CoinsListAdapter.class);


        views.setRemoteAdapter(R.id.list, intent);


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

        //loadSerialCoins(context);
        //restorePreferences(context);
        //itemListener = coinSymbol -> {
//            Log.e("ITEM SELECTED", coinSymbol);
//
//            // TODO: Show chart
//        };
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        // for (int appWidgetId : appWidgetIds) {
        //    CoinListWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

//    private void restorePreferences(Context context) {
//        coinsToObserve = new ArrayList<>();
//        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_WIDGET);
//
//        if (!storedPreferences.isEmpty()) {
//
//            String[] parts = storedPreferences.split(" ,");
//
//            coinsToObserve.addAll(Arrays.asList(parts));
//            loadSerialCoins(context);
//            getCoinsToShow();
//            makeToast(context, coinsToObserve.toString());
//            Log.e("PREFERENCES-Restored", coinsToObserve.toString());
//        }
//    }

    private void makeToast(Context context, String text_content) {
        Log.e("Toast", text_content);
        Toast.makeText(context, text_content, Toast.LENGTH_SHORT).show();
    }

//    private Coin getCoinBySymbol(String coinSymbol) {
//
//        for (Coin coin : coins) {
//
//            Log.e("COIN-SYMBOL", coin.getSymbol());
//            Log.e("COIN-SYMBOL", coin.getId());
//            Log.e("COIN-SYMBOL-STORED", coinSymbol);
//            if (coin.getSymbol().contains(coinSymbol)) {
//                Log.e("COIN-FOUND", coinSymbol);
//
//                return coin;
//            }
//        }
//        return null;
//    }
//
//    private void getCoinsToShow() {
//        ArrayList<Coin> coinsToShow = new ArrayList<>();
//
//        Log.e("COINS", coins.toString());
//        List<String> coinsToFind = Arrays.asList(coinsToObserve.toString().split(","));
//        Log.e("COINTOFIND", coinsToFind.toString());
//
//        for (String coin : coinsToFind) {
//            coin = coin.replace("[", "").replace("]", "");
//
//            if (!coin.isEmpty()) {
//                Log.e("COINTOFIND-SYM", coin);
//
//                Coin coinToAdd = getCoinBySymbol(coin);
//                assert coinToAdd != null;
//                Log.e("COINTOADD", coinToAdd.toString());
//                coinsToShow.add(coinToAdd);
//
//            }
//        }
//    }
//
//    private void loadSerialCoins(Context context) {
//        String serialCoins = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_CACHE);
//        Type listType = new TypeToken<ArrayList<Coin>>() {
//        }.getType();
//        coins = (new Gson()).fromJson(serialCoins, listType);
//        Log.e("LOAD_COINS", coins.toString());
//    }
}

