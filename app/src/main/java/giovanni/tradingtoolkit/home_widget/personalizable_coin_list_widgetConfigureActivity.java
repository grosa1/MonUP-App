package giovanni.tradingtoolkit.home_widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.marketprices.CoinsListAdapter;

/**
 * The configuration screen for the {@link personalizable_coin_list_widget personalizable_coin_list_widget} AppWidget.
 */
public class personalizable_coin_list_widgetConfigureActivity extends Activity {

    //  private static final String PREFS_NAME = "giovanni.tradingtoolkit.home_widget.personalizable_coin_list_widget";
    // private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final int SELECTED_COIN = 0;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText textArea;

    RecyclerView recyclerView;
    private List<Coin> coins;
    private List<Coin> coinsToObserve;
    private ArrayList<Coin> filteredList;
    private Context context;
    private CoinsListAdapter.CoinItemListener itemListener;

    View.OnClickListener addWidgetBtnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = personalizable_coin_list_widgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            //  String widgetText = textArea.getText().toString();
            //  saveTitlePref(context, mAppWidgetId, widgetText);
            //  makeToast(widgetText);

            storePreferences();
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            personalizable_coin_list_widget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    View.OnClickListener addCoinBtnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            String requested_coin = textArea.getText().toString();

            filter(requested_coin);
            if (filteredList.size() != 1) {

                makeToast("Insert correct name or select once from list");
            } else {

                setToObserve(filteredList.get(SELECTED_COIN));
            }
        }
    };

    public personalizable_coin_list_widgetConfigureActivity() {
        super();
    }

//    // Write the prefix to the SharedPreferences object for this widget
//    static void saveTitlePref(Context context, int appWidgetId, String text) {
//        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
//        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
//        prefs.apply();
//    }
//
//    // Read the prefix from the SharedPreferences object for this widget.
//    // If there is no preference saved, get the default from a resource
//    static String loadTitlePref(Context context, int appWidgetId) {
//        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
//        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
//        if (titleValue != null) {
//            return titleValue;
//        } else {
//            return context.getString(R.string.appwidget_text);
//        }
//    }
//
//    static void deleteTitlePref(Context context, int appWidgetId) {
//        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
//        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
//        prefs.apply();
//    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.personalizable_coin_list_widget_configure);

        context = personalizable_coin_list_widgetConfigureActivity.this;
        coinsToObserve = new ArrayList<>();
        textArea = (EditText) findViewById(R.id.appwidget_text);
        recyclerView = (RecyclerView) findViewById(R.id.search_view);
        findViewById(R.id.add_coin_button).setOnClickListener(addCoinBtnClickListener);
        findViewById(R.id.add_widget_button).setOnClickListener(addWidgetBtnClickListener);
        loadSerialCoins();
        restorePreferences();


        itemListener = new CoinsListAdapter.CoinItemListener() {
            @Override
            public void onCoinClick(String coinSymbol) {

                Log.e("ITEM SELECTED", coinSymbol);
                filter(coinSymbol);
                if (filteredList.size() == 1){

                    setToObserve(filteredList.get(SELECTED_COIN));
                }
            }
        };

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

        // textArea.setText(loadTitlePref(personalizable_coin_list_widgetConfigureActivity.this, mAppWidgetId));
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        String coinName;
        String coinSymbol;
        for (Coin coin : coins) {
            coinName = (coin.getName()).toLowerCase();
            coinSymbol = (coin.getSymbol()).toLowerCase();

            if (coinName.contains(text.toLowerCase()) | coinSymbol.contains(text.toLowerCase())) {
                filteredList.add(coin);

                Log.e("-----Coin: ", coin.toString() + " Symbol: " + coin.getSymbol() + " Name: " + coin.getName());
            }
        }
        CoinsListAdapter coinsListAdapter = new CoinsListAdapter(context, coins, itemListener);
        coinsListAdapter.filterList(filteredList);
        recyclerView.setAdapter(coinsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setToObserve(Coin coin) {
        coinsToObserve.add(coin);
        makeToast("Coin added to Observer" + coin);

        for (int i = 0; i < coinsToObserve.size(); i++) {Log.e("COINS TO OBSERVE", coinsToObserve.get(coinsToObserve.size() - i - 1).toString()); }
    }

    private void makeToast(String text_content) {
        Log.e("Toast", text_content);
        Toast.makeText(this, text_content, Toast.LENGTH_SHORT).show();
    }

    private void storePreferences() {
        SharedPrefs.storeString(context, SharedPrefs.KEY_COINS_WIDGET, coinsToObserve.toString());
        Log.e("PREFERENCES", "Coins Stored");
        Toast.makeText(this, "Coins Stored", Toast.LENGTH_SHORT).show();
    }

    private void restorePreferences() {
        String storedPreferences = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_WIDGET);

        if( !storedPreferences.isEmpty()) {

            // coinsToObserve = //TODO assign value of preferences
            Log.e("PREFERENCES", storedPreferences);
        }

        Log.e("PREFERENCES", "Coins Restored");
        Toast.makeText(this, "Coins Restored", Toast.LENGTH_SHORT).show();
    }

    private void loadSerialCoins() {
        String serialCoins = SharedPrefs.restoreString(context, SharedPrefs.KEY_COINS_CACHE);
        Type listType = new TypeToken<ArrayList<Coin>>() {
        }.getType();
        coins = (new Gson()).fromJson(serialCoins, listType);

        Log.e("LOADED-SERIALCOINS", serialCoins);
        Log.e("LOAD_COINS", coins.toString());
    }
}

