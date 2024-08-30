package giovanni.tradingtoolkit.marketprices;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.SharedPrefs;
import giovanni.tradingtoolkit.main.ToastManager;
import giovanni.tradingtoolkit.marketprices.remote.CoinMarketCapService;
import giovanni.tradingtoolkit.marketprices.remote.RetrofitClient;
import giovanni.tradingtoolkit.marketprices.remote.model.Variation;
import giovanni.tradingtoolkit.marketprices.remote.model.coin_response.Coin;
import giovanni.tradingtoolkit.marketprices.remote.model.coin_response.ResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsFragment extends Fragment {
    public static final String ARG_CHART_SYM = "price_data";
    public static final String ARG_CHART_CURRENCY = "currency_data";
    public static final String DEFAULT_CURRENCY = "USD";
    public static final String LIST_LIMIT = "200";

    public static final String CURRENCY_EUR = "EUR";
    public static final String CURRENCY_BTC = "BTC";

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout pullDown;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.tv_coin_name)
    TextView tvCoin;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.spinner_currency)
    Spinner spinnerCurrency;
    @BindView(R.id.tv_percentage_variation)
    TextView tvVariation;

    public static String currency;
    private List<Coin> coins;
    private CoinsListAdapter listAdapter;
    private CoinMarketCapService coinDataService;
    private boolean isConnected = false;

    private Variation currentVariation;
    private boolean sortPriceAsc, sortNameAsc, sortRankAsc;

    public CoinsFragment() {
        coins = new ArrayList<>();
        currentVariation = Variation.Daily;
    }

    public static CoinsFragment newInstance() {
        CoinsFragment fragment = new CoinsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.coinDataService = RetrofitClient.getCoinMarketCapService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.coins_list_fragment, container, false);
        ButterKnife.bind(this, view);

        currency = SharedPrefs.restoreString(requireContext(), SharedPrefs.KEY_CURRENCY);
        if (currency.isEmpty()) {
            currency = DEFAULT_CURRENCY;
        }

        listAdapter = new CoinsListAdapter(getContext(), coins, coinSym -> {
            if (!(currency.equals(CoinsFragment.CURRENCY_BTC) && coinSym.equals(CoinsFragment.CURRENCY_BTC))) {
                this.showChart(coinSym, currency);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);

        //API CALL
        try {
            this.loadCoinList(currency, LIST_LIMIT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SET CURRENCY SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.currency_array, R.layout.layout_currency_spinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerCurrency.setAdapter(adapter);

        this.resetSpinner();
        this.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isConnected) {
                    CoinsFragment.currency = spinnerCurrency.getItemAtPosition(position).toString().trim();
                    SharedPrefs.storeString(requireContext(), SharedPrefs.KEY_CURRENCY, CoinsFragment.currency);
                    updateList(CoinsFragment.currency);
                } else {
                    resetSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //SET PULL DOWN LISTENER
        pullDown.setOnRefreshListener(() -> updateList(currency));

        //SET LIST SORTING
        tvVariation.setText(getContext().getString(R.string.percentage_variation_title));

        // Sort by rank
        tvRank.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortNameAsc = true;
            sortPriceAsc = true;

            Collections.sort(coins, (coin1, coin2) -> coin1.getRank().compareTo(coin2.getRank()));
            if (sortRankAsc) {
                sortRankAsc = false;
            } else {
                sortRankAsc = true;
                Collections.reverse(coins);
            }

            resetToolbarSortButtons();
            tvRank.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            listAdapter.notifyDataSetChanged();
        });

        // Sort by name
        tvCoin.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortRankAsc = true;
            sortPriceAsc = true;

            Collections.sort(coins, (coin1, coin2) -> coin1.getName().compareTo(coin2.getName()));
            if (sortNameAsc) {
                sortNameAsc = false;
            } else {
                sortNameAsc = true;
                Collections.reverse(coins);
            }

            resetToolbarSortButtons();
            tvCoin.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            listAdapter.notifyDataSetChanged();
        });

        // Sort by price
        tvPrice.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortRankAsc = true;
            sortNameAsc = true;

            switch (currency) {
                case CoinsFragment.DEFAULT_CURRENCY:
                    Collections.sort(coins, (coin1, coin2) -> coin1.getPriceUsd().compareTo(coin2.getPriceUsd()));
                    break;
                case CoinsFragment.CURRENCY_EUR:
                    Collections.sort(coins, (coin1, coin2) -> coin1.getPriceEur().compareTo(coin2.getPriceEur()));
                    break;
                case CoinsFragment.CURRENCY_BTC:
                    Collections.sort(coins, (coin1, coin2) -> coin1.getPriceBtc().compareTo(coin2.getPriceBtc()));
                    break;
            }

            if (sortPriceAsc) {
                sortPriceAsc = false;
            } else {
                sortPriceAsc = true;
                Collections.reverse(coins);
            }
            resetToolbarSortButtons();
            tvPrice.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            listAdapter.notifyDataSetChanged();
        });

        // Sort by percentage variation
        tvVariation.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortRankAsc = true;
            sortNameAsc = true;
            // sortPriceAsc = true;

            String newVariation = "";
            switch (currentVariation) {
                case Weekly:    // IF WEEKLY SET TO HOUR
                    newVariation = getContext().getString(R.string.weekly);
                    Collections.sort(coins, (coin1, coin2) -> coin2.getPercentChange1h().compareTo(coin1.getPercentChange1h()));
                    currentVariation = Variation.Hour;
                    break;

                case Hour:    // IF HOUR SET TO DAILY
                    newVariation = getContext().getString(R.string.hour);
                    Collections.sort(coins, (coin1, coin2) -> coin2.getPercentChange24h().compareTo(coin1.getPercentChange24h()));
                    currentVariation = Variation.Daily;
                    break;

                case Daily:    // IF DAILY SET TO WEEKLY
                    newVariation = getContext().getString(R.string.daily);
                    Collections.sort(coins, (coin1, coin2) -> coin2.getPercentChange7d().compareTo(coin1.getPercentChange7d()));
                    currentVariation = Variation.Weekly;
                    break;
            }
            listAdapter.notifyDataSetChanged();

            resetToolbarSortButtons();
            tvVariation.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvVariation.setText((getContext().getString(R.string.percentage_variation_sym)) + " " + newVariation);
        });

        return view;
    }

    private void resetToolbarSortButtons() {
        tvRank.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvCoin.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvPrice.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvVariation.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvVariation.setText((requireContext().getString(R.string.percentage_variation_title)));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialogManager.open(getContext());
    }

    public void loadCoinList(String currencyType, String limit) throws IOException {
        if (currencyType.isEmpty() || currencyType.isBlank()) {
            currencyType = DEFAULT_CURRENCY;
        }

        this.coinDataService.getList(currencyType, limit).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                // TODO: refactor using try-with-resources
                if (response.body() != null) {
                    Log.d("RES", response.body().toString());
                }

                if (response.isSuccessful()) {
                    ResponseData body = response.body();
                    if (null != body) {
                        isConnected = true;
                        coins = body.getData();
                        storeCache(coins);
                        listAdapter.updateCoinsList(coins);
                    }

                    ProgressDialogManager.close();

                    if (pullDown != null) {
                        pullDown.setRefreshing(false);
                    }

                } else {
                    isConnected = false;

                    String error_msg = "";
                    if (response.message() != null) {
                        error_msg = "\n" + response.message();
                    }
                    Log.e("API_ERROR", "Error calling coin list API with code " + response.code() + error_msg);

                    ProgressDialogManager.close();
                    if (pullDown != null) {
                        pullDown.setRefreshing(false);
                    }

                    if (restoreCache()) {
                        ToastManager.create(getContext(), R.string.coins_request_error);
                    } else {
                        ToastManager.makeAlert(getContext(), "Error", getResources().getString(R.string.coins_request_error));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {
                isConnected = false;
                boolean isCache = restoreCache();
                ProgressDialogManager.close();

                if (pullDown != null) {
                    pullDown.setRefreshing(false);
                }

                if (isCache) {
                    ToastManager.create(getContext(), R.string.coins_request_error);
                } else {
                    ToastManager.makeAlert(getContext(), "Error", getResources().getString(R.string.coins_request_error));
                }
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }

    public void showChart(final String coinSymbol, final String currencyType) {
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        intent.putExtra(ARG_CHART_SYM, coinSymbol);
        intent.putExtra(ARG_CHART_CURRENCY, currencyType);
        startActivity(intent);
    }

    private void updateList(String currencyName) {
        try {
            tvVariation.setText((requireContext().getString(R.string.percentage_variation_title)));
            loadCoinList(currencyName, LIST_LIMIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean restoreCache() {
        String serialCoins = SharedPrefs.restoreString(requireContext(), SharedPrefs.KEY_COINS_CACHE);
        if (!serialCoins.isEmpty()) {
            Type listType = new TypeToken<ArrayList<Coin>>() {
            }.getType();
            List<Coin> cached = (new Gson()).fromJson(serialCoins, listType);
            coins = cached;
            listAdapter.updateCoinsList(cached);
            return true;
        }
        return false;
    }

    private void storeCache(final List<Coin> updatedCoins) {
        if (getContext() == null) {
            Log.e("storeCache", "Unable to store cache for current fragment");
            return;
        }

        String serialCoins = (new Gson()).toJson(updatedCoins);
        SharedPrefs.storeString(requireContext(), SharedPrefs.KEY_COINS_CACHE, serialCoins);
    }

    private void resetSpinner() {
        if (this.spinnerCurrency != null) {
            int position;
            if (currency.equals(CoinsFragment.CURRENCY_EUR)) {
                position = 1;
            } else if (currency.equals(CoinsFragment.CURRENCY_BTC)) {
                position = 2;
            } else {
                position = 0;
            }
            this.spinnerCurrency.setSelection(position);
        }
    }
}
