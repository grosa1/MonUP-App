package pz_apps.monup.home_widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pz_apps.monup.data.model.coin_response.Coin;
import pz_apps.monup.data.remote.LoadCoinReceiver;
import pz_apps.monup.main.ResourcesLoader;
import pz_apps.monup.main.SharedPrefs;
import pz_apps.monup.marketprices.CoinsListAdapter;
import pz_apps.monup.R;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private ArrayList<Coin> coinsToShow;
    private String coinsToObserve;
    private List<Coin> coins;
    private CoinsListAdapter.CoinItemListener itemListener;

    WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("AppWidgetId", String.valueOf(appWidgetId));
        viewInit();

        //Calling Service Refresh
        Intent broadcastIntent = new Intent(context, LoadCoinReceiver.class);
        context.sendBroadcast(broadcastIntent);
    }

    private void updateWidgetListView() {
        viewInit();
        for (int i = 0; i < coinsToShow.size(); i++) {
            getViewAt(i);
        }
    }

    private void viewInit() {
        loadSerialCoins(context);
        restorePreferences(context);
        getCoinsToShow();
        itemListener = coinSymbol -> {
            //TODO: handle click as in CoinListWidget for refresh and action at item click
        };
        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coinsToShow, itemListener);
        coinsListAdapter.filterList(coinsToShow);
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
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_coin_list_item);
        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coinsToShow, itemListener);
        coinsListAdapter.filterList(coinsToShow);
        Intent intent = new Intent(context, coinsListAdapter.getClass());
        Coin coinItem = coinsToShow.get(position);
        remoteView = setItemInfo(remoteView, coinItem, position);
        remoteView.setRemoteAdapter(R.id.list, intent);
        return remoteView;
    }

    private RemoteViews setItemInfo(RemoteViews rv, Coin coinItem, int position) {
        int iconToLoad = ResourcesLoader.getResId(coinItem.getSymbol().toLowerCase());
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
        decimalFormat.setMinimumFractionDigits(2);
        String numberAsString = decimalFormat.format(coinItem.getPriceUsd());

        rv.setTextViewText(R.id.coin_name, coinItem.getName());
        rv.setTextViewText(R.id.list_position, "TODO");
        rv.setTextViewText(R.id.price, numberAsString);
        rv.setTextViewText(R.id.percentage_variation_1h, setPercentageText(coinItem.getPercentChange1h()));
        rv.setTextViewText(R.id.percentage_variation_1d, setPercentageText(coinItem.getPercentChange24h()));
        rv.setTextViewText(R.id.percentage_variation_1w, setPercentageText(coinItem.getPercentChange7d()));
        rv.setImageViewResource(R.id.icon, iconToLoad);
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

    private String setPercentageText(Double value) {
        String percentage;
        if (value < 0) {
            percentage = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(value), "%");
        } else {
            percentage = String.format(Locale.getDefault(), "+ %.2f%s", value, "%");
        }

        return percentage;
    }

    private Coin getCoinBySymbol(String coinSymbol) {
        for (Coin coin : coins) {
            if (coin.getSymbol().contains(coinSymbol)) {
                return coin;
            }
        }
        return null;
    }

    private void getCoinsToShow() {
        coinsToShow = new ArrayList<>();
        String[] coinsToFind = coinsToObserve.split(",");
        for (String coin : coinsToFind) {
            coin = coin.replace("[", "").replace("]", "");
            if (!coin.isEmpty() && (getCoinBySymbol(coin) != null)) {
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
        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_WIDGET_COINS);
        if (storedPreferences != null && !storedPreferences.isEmpty()) {
            coinsToObserve = storedPreferences;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onCreate() {
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged() {
        updateWidgetListView();
    }

    @Override
    public void onDestroy() {
        String INVALID_WIDGET_ID = Integer.toString(CoinListWidgetConfigureActivity.INVALID_WIDGET_ID);
        SharedPrefs.storeString(context, SharedPrefs.KEY_WIDGET_ID, INVALID_WIDGET_ID);
    }
}