package giovanni.tradingtoolkit.home_widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import giovanni.tradingtoolkit.data.model.coin_response.Coin;
import giovanni.tradingtoolkit.marketprices.CoinsListAdapter;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import giovanni.tradingtoolkit.R;

/**
 * The configuration screen for the {@link CoinListWidget CoinListWidget} AppWidget.
 */
public class CoinListWidgetConfigureActivity extends Activity {

    private static final int SELECTED_COIN = 0;
    public static final int INVALID_WIDGET_ID = -1;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText textArea;
    RecyclerView recyclerView;
    RecyclerView observedCoinListView;

    private List<Coin> coins;
    private String coinsToObserve;
    private ArrayList<Coin> filteredList;
    private ArrayList<Coin> coinsToShow;
    private Context context;

    View.OnClickListener addCoinBtnClickListener = v -> {
        String requested_coin = textArea.getText().toString();
        filter(requested_coin);
        if (filteredList.size() != 1) {
            ToastManager.create(this, R.string.incorrect_coin_name);
        } else {
            setToObserve(filteredList.get(SELECTED_COIN).getSymbol());
        }
    };

    View.OnClickListener addWidgetBtnClickListener = v -> {
        if (coinsToObserve.isEmpty()) {
            ToastManager.create(this, getResources().getString(R.string.no_coin_to_observe));
        } else {
            if (getWidgetNumber() == INVALID_WIDGET_ID) {
                storeWidgetNumber();

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getWidgetNumber());
                setResult(RESULT_OK, resultValue);
            }
            CoinListWidget.updateWidget(context);
            storePreferences();
            finish();
        }
    };

    public CoinListWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_coin_list_configure);

        context = CoinListWidgetConfigureActivity.this;
        textArea = findViewById(R.id.appwidget_text);
        recyclerView = findViewById(R.id.search_view);
        observedCoinListView = findViewById(R.id.coins_observed_view);

        findViewById(R.id.add_coin_button).setOnClickListener(addCoinBtnClickListener);
        findViewById(R.id.add_widget_button).setOnClickListener(addWidgetBtnClickListener);

        loadSerialCoins();
        restorePreferences();
        loadObservedCoinListView();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void loadObservedCoinListView() {
        if (coinsToObserve != null) {
            coinsToShow = new ArrayList<>();
            String[] coinsToFind;
            coinsToFind = coinsToObserve.split(",");

            for (String coin : coinsToFind) {
                coin = coin.replace("[", "").replace("]", "");

                if (!coin.isEmpty()) {
                    Coin coinToAdd = getCoinBySymbol(coin);
                    assert coinToAdd != null;
                    coinsToShow.add(coinToAdd);
                }
            }
            refreshObservedCoinRecycleView();
        }
    }

    private void removeCoinBySymbol(String coinSymbol) {
        String newPreferences = (coinsToObserve).replace(coinSymbol, "").replace(",,", ",");
        if (newPreferences.startsWith(",")) {
            newPreferences = newPreferences.substring(1);
        }

        if (newPreferences.endsWith(",")) {
            newPreferences = newPreferences.substring(0, newPreferences.length() - 1);
        }

        if (coinsToObserve.equals(coinSymbol)) {
            newPreferences = "";
        }
        coinsToObserve = newPreferences;
        storePreferences();
        loadObservedCoinListView();
    }

    private Coin getCoinBySymbol(String coinSymbol) {
        for (Coin coin : coins) {
            if (coin.getSymbol().contains(coinSymbol)) {
                return coin;
            }
        }
        return null;
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        String coinName;
        String coinSymbol;
        for (Coin coin : coins) {
            coinName = (coin.getName()).toLowerCase();
            coinSymbol = (coin.getSymbol()).toLowerCase();
            if (coinName.contains(text.toLowerCase()) || coinSymbol.contains(text.toLowerCase())) {
                filteredList.add(coin);
            }
        }
        refreshSearchRecycleView();
    }

    private void refreshSearchRecycleView() {
        CoinsListAdapter.CoinItemListener itemListener = coinSymbol -> setToObserve(coinSymbol.toUpperCase());
        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coins, itemListener);
        coinsListAdapter.filterList(filteredList);
        recyclerView.setAdapter(coinsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void refreshObservedCoinRecycleView() {
        CoinsListAdapter.CoinItemListener removeCoinListener = coinSymbol -> {
            removeCoinBySymbol(coinSymbol);
            ToastManager.create(this, getResources().getString(R.string.coin_removed));
        };
        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coinsToShow, removeCoinListener);
        observedCoinListView.setAdapter(coinsListAdapter);
        observedCoinListView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setToObserve(String coinSymbol) {
        if (!coinsToObserve.contains(coinSymbol)) {
            coinsToObserve = coinsToObserve + "," + coinSymbol.toUpperCase();
            loadObservedCoinListView();
            refreshObservedCoinRecycleView();
            ToastManager.create(this, getResources().getString(R.string.coin_added_to_observer) + coinSymbol);
        } else {
            ToastManager.create(this, getResources().getString(R.string.coin_already_observed));
        }
    }

    private void storePreferences() {
        if (coinsToObserve != null) {
            SharedPrefs.storeString(context, SharedPrefs.KEY_WIDGET_COINS, coinsToObserve);
        }
    }

    private void restorePreferences() {
        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_WIDGET_COINS);
        if (storedPreferences != null) {
            coinsToObserve = storedPreferences;
        } else {
            coinsToObserve = "";
        }
    }

    private void loadSerialCoins() {
        String serialCoins = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_CACHE);
        Type listType = new TypeToken<ArrayList<Coin>>() {
        }.getType();
        coins = (new Gson()).fromJson(serialCoins, listType);
        Log.e("ERR: ", "loadSerialCoins: " + coins);
    }

    private void storeWidgetNumber() {
        if (mAppWidgetId != INVALID_WIDGET_ID && mAppWidgetId != getWidgetNumber()) {
            SharedPrefs.storeString(context, SharedPrefs.KEY_WIDGET_ID, Integer.toString(mAppWidgetId));
        }
    }

    private int getWidgetNumber() {
        String widgetId = SharedPrefs.restoreString(context, SharedPrefs.KEY_WIDGET_ID);
        if (widgetId != null && !widgetId.isEmpty()) {
            return Integer.parseInt(widgetId);
        }
        return INVALID_WIDGET_ID;
    }

    @Override
    protected void onDestroy() { //TODO: remove isLoadCoinServiceRunning making same method and loadCoins() in Main Activity static
        CoinListWidget.runService(context);
        super.onDestroy();
    }
}

