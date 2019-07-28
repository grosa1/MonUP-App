package giovanni.tradingtoolkit.home_widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context context = null;
    private int appWidgetId;
    private ArrayList<Coin> coinsToShow;
    private ArrayList<String> coinsToObserve;
    private List<Coin> coins;
    private CoinsListAdapter.CoinItemListener itemListener;


    private List<String> widgetList = new ArrayList<String>();
    //private DBHelper dbhelper;

    public WidgetRemoteViewsFactory(Context context, Intent intent)
    {
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

        //Intent intent = new Intent(context, coinsListAdapter.getClass());
        //remoteView.setRemoteAdapter(R.id.list, intent);

        //dbhelper = new DBHelper(this.context);
    }

    private void updateWidgetListView()
    {
        //String[] coins = {"ETH","BTC","BNB","ZEC"}; // TODO: da eliminare
        //List<String> convertedToList = new ArrayList<String>(Arrays.asList(coins));
        //this.widgetList = convertedToList;
    }

    @Override
    public int getCount()
    {
        return coinsToShow.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.coin_list_item);

        Log.d("Loading", coinsToShow.get(position).toString());

        itemListener = coinSymbol -> {
            Log.e("ITEM SELECTED", coinSymbol);

        };

        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coinsToShow, itemListener);
        coinsListAdapter.filterList(coinsToShow);

        Intent intent = new Intent(context, coinsListAdapter.getClass());
        Coin coinItem = coinsToShow.get(position);
        remoteView.setTextViewText(R.id.coin_name, coinItem.getName());
        remoteView.setTextViewText(R.id.list_position, "TODO");
        // remoteView.setTextViewText(R.id.icon, "TODO");
        remoteView.setTextViewText(R.id.price, coinItem.getPriceBtc());
        remoteView.setTextViewText(R.id.percentage_variation_1h, Float.toString((float)coinItem.getPercentChange1h()));
        remoteView.setTextViewText(R.id.percentage_variation_1d, Float.toString((float)coinItem.getPercentChange24h()));
        remoteView.setTextViewText(R.id.percentage_variation_1w, Float.toString((float)coinItem.getPercentChange7d()));


        remoteView.setRemoteAdapter(R.id.list, intent);

        //remoteView.setTextViewText(R.id.list, coinsToShow.get(position).getSymbol());
        Log.d("CHECK", coinsToShow.get(position).getSymbol());
        return remoteView;
    }

    @Override
    public int getViewTypeCount()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged()
    {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        coinsToShow.clear();
      //  dbhelper.close();
    }

    private Coin getCoinBySymbol(String coinSymbol) {

        for (Coin coin : coins) {

            Log.e("COIN-SYMBOL", coin.getSymbol());
            Log.e("COIN-SYMBOL", coin.getId());
            Log.e("COIN-SYMBOL-STORED", coinSymbol);
            if (coin.getSymbol().contains(coinSymbol)) {
                Log.e("COIN-FOUND", coinSymbol);

                return coin;
            }
        }
        return null;
    }

    private void getCoinsToShow() {
        coinsToShow = new ArrayList<>();

        Log.e("COINS", coins.toString());
        List<String> coinsToFind = Arrays.asList(coinsToObserve.toString().split(","));
        Log.e("COINTOFIND", coinsToFind.toString());

        for (String coin : coinsToFind) {
            coin = coin.replace("[", "").replace("]", "");

            if (!coin.isEmpty()) {
                Log.e("COINTOFIND-SYM", coin);

                Coin coinToAdd = getCoinBySymbol(coin);
                assert coinToAdd != null;
                Log.e("COINTOADD", coinToAdd.toString());
                coinsToShow.add(coinToAdd);

            }
        }
    }

    private void loadSerialCoins(Context context) {
        String serialCoins = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_CACHE);
        Type listType = new TypeToken<ArrayList<Coin>>() {
        }.getType();
        coins = (new Gson()).fromJson(serialCoins, listType);
        Log.e("LOAD_COINS", coins.toString());
    }

    private void restorePreferences(Context context) {
        coinsToObserve = new ArrayList<>();
        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_WIDGET);

        if (!storedPreferences.isEmpty()) {

            String[] parts = storedPreferences.split(" ,");

            coinsToObserve.addAll(Arrays.asList(parts));
            Log.e("PREFERENCES-Restored", coinsToObserve.toString());
        }
    }
}