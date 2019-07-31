package giovanni.tradingtoolkit.home_widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.marketprices.CoinsListAdapter;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId;
    private ArrayList<Coin> coinsToShow;
    private ArrayList<String> coinsToObserve;
    private List<Coin> coins;
    private CoinsListAdapter.CoinItemListener itemListener;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("AppWidgetId", String.valueOf(appWidgetId));

        loadSerialCoins(context);
        restorePreferences(context);
        getCoinsToShow();

        itemListener = coinSymbol -> {
            Log.e("ITEM SELECTED", coinSymbol);

        };

        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coinsToShow, itemListener);
        coinsListAdapter.filterList(coinsToShow);
    }

    private void updateWidgetListView() {
        //TODO: Refresh dati
        for (int i = 0; i < coinsToShow.size(); i++) {
            getViewAt(i);

            Log.d("WIDGETUPDATED", "POSITION: " + i);
        }
    }

    @Override
    public int getCount() {
        return coinsToShow.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_coin_list_item);

        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coinsToShow, itemListener);
        coinsListAdapter.filterList(coinsToShow);

        Intent intent = new Intent(context, coinsListAdapter.getClass());
        Coin coinItem = coinsToShow.get(position);

        remoteView = setItemInfo(remoteView, coinItem, position);
        remoteView.setRemoteAdapter(R.id.list, intent);

        return remoteView;
    }

    private RemoteViews setItemInfo(RemoteViews rv, Coin coinItem, int position) {

        rv.setTextViewText(R.id.coin_name, coinItem.getName());
        rv.setTextViewText(R.id.list_position, "TODO");
        // rv.setTextViewText(R.id.icon, "TODO");
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        String numberAsString = decimalFormat.format(coinItem.getPriceUsd());

        rv.setTextViewText(R.id.price, numberAsString);
        rv.setTextViewText(R.id.percentage_variation_1h, Double.toString(coinItem.getPercentChange1h()));
        rv.setTextViewText(R.id.percentage_variation_1d, Double.toString(coinItem.getPercentChange24h()));
        rv.setTextViewText(R.id.percentage_variation_1w, Double.toString(coinItem.getPercentChange7d()));

        rv.setTextColor(R.id.percentage_variation_1d, setPercentageColor(coinItem.getPercentChange24h()));
        rv.setTextColor(R.id.percentage_variation_1w, setPercentageColor(coinItem.getPercentChange7d()));

        if (setPercentageColor(coinItem.getPercentChange1h()) == context.getResources().getColor(R.color.widgetGreen)) {

            rv.setTextColor(R.id.percentage_variation_1h, context.getResources().getColor(R.color.widgetGreen));
            rv.setTextColor(R.id.price, context.getResources().getColor(R.color.widgetGreen));
        } else {

            rv.setTextColor(R.id.percentage_variation_1h, context.getResources().getColor(R.color.widgetRed));
            rv.setTextColor(R.id.price, context.getResources().getColor(R.color.widgetRed));
        }

        if (position % 2 == 0) {
            rv.setInt(R.id.coin_list_item, "setBackgroundColor", context.getResources().getColor(R.color.widgetItemPrimaryColor));
        } else {
            rv.setInt(R.id.coin_list_item, "setBackgroundColor", context.getResources().getColor(R.color.widgetItemSecondaryColor));
        }
        return rv;
    }

    private int setPercentageColor(Double value) {

        if (value >= 0) {
            return context.getResources().getColor(R.color.widgetGreen);
        } else {
            return context.getResources().getColor(R.color.widgetRed);
        }
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged() {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        coinsToShow.clear();
    }

    private Coin getCoinBySymbol(String coinSymbol) {

        for (Coin coin : coins) {
            if (coin.getSymbol().contains(coinSymbol)) {
                Log.e("COIN-FOUND", coinSymbol);
                return coin;
            }
        }
        return null;
    }

    private void getCoinsToShow() {
        coinsToShow = new ArrayList<>();
        String[] coinsToFind = coinsToObserve.toString().split(",");

        for (String coin : coinsToFind) {
            coin = coin.replace("[", "").replace("]", "");

            if (!coin.isEmpty()) {
                Coin coinToAdd = getCoinBySymbol(coin);
                assert coinToAdd != null;
                coinsToShow.add(coinToAdd);
            }
        }
    }

    private void loadSerialCoins(Context context) {
        String serialCoins = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_CACHE);
        Type listType = new TypeToken<ArrayList<Coin>>() {
        }.getType();
        coins = (new Gson()).fromJson(serialCoins, listType);
    }

    private void restorePreferences(Context context) {
        coinsToObserve = new ArrayList<>();
        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_WIDGET);

        if (!storedPreferences.isEmpty()) {

            String[] parts = storedPreferences.split(" ,");
            coinsToObserve.addAll(Arrays.asList(parts));
        }
    }
}