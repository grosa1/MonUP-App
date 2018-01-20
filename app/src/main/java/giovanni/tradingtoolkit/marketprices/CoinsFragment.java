package giovanni.tradingtoolkit.marketprices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.data.model.Variation;
import giovanni.tradingtoolkit.data.remote.CoinMarketCapService;
import giovanni.tradingtoolkit.data.remote.RetrofitClient;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.ToastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsFragment extends Fragment {

    public static final String ARG_PRICE_DATA = "price_data";
    public static final String DEFAULT_CURRENCY = "EUR";
    public static final String LIST_LIMIT = "200";

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
    @BindView(R.id.tv_percentage_variation)
    TextView tvVariation;

    public static final String ARG_COINS = "COINS";
    private List<Coin> coins;
    private CoinsListAdapter listAdapter;
    private CoinMarketCapService coinDataService;

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

        if (getArguments() != null) {
            //TODO:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.coins_list_fragment, container, false);
        ButterKnife.bind(this, view);

        listAdapter = new CoinsListAdapter(getContext(), coins, coinSym -> {
//            ToastManager.create(getContext(), "Ƀ = " + priceBtc);
            this.showChart(coinSym);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);

        try {
            loadCoinList(DEFAULT_CURRENCY, LIST_LIMIT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pullDown.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    loadCoinList(DEFAULT_CURRENCY, LIST_LIMIT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        String variation = "";
        switch (currentVariation) {
            case Hour:
                variation = getContext().getString(R.string.hour);
                break;
            case Daily:
                variation = getContext().getString(R.string.daily);
                break;
            case Weekly:
                variation = getContext().getString(R.string.weekly);
                break;
        }
        tvVariation.setText(String.format(getContext().getString(R.string.percentage_variation_title), variation));

        // Sort by rank
        tvRank.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortNameAsc = true;
            sortPriceAsc = true;

            Collections.sort(coins, (coin1, coin2) -> Integer.valueOf(coin1.getRank()).compareTo(coin2.getRank()));
            if (sortRankAsc) {
                sortRankAsc = false;
            } else {
                sortRankAsc = true;
                Collections.reverse(coins);
            }
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
            listAdapter.notifyDataSetChanged();
        });

        // Sort by price
        tvPrice.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortRankAsc = true;
            sortNameAsc = true;

            Collections.sort(coins, (coin1, coin2) -> coin1.getPriceEur().compareTo(coin2.getPriceEur()));
            if (sortPriceAsc) {
                sortPriceAsc = false;
            } else {
                sortPriceAsc = true;
                Collections.reverse(coins);
            }
            listAdapter.notifyDataSetChanged();


        });

        // Sort by percentage variation
        tvVariation.setOnClickListener(tv -> {
            // Initialize the sorting order of the other two attributes
            sortRankAsc = true;
            sortNameAsc = true;
            sortPriceAsc = true;

            String var = "";
            switch (currentVariation) {
                case Hour:    // Show last day variations sorted by % desc
                    var = getContext().getString(R.string.hour);
                    Collections.sort(coins, (coin1, coin2) -> Double.valueOf(coin2.getPercentChange1h()).compareTo(coin1.getPercentChange1h()));
                    currentVariation = Variation.Daily;
                    break;

                case Daily:    // Show last week variations sorted by % desc
                    var = getContext().getString(R.string.daily);
                    Collections.sort(coins, (coin1, coin2) -> Double.valueOf(coin2.getPercentChange24h()).compareTo(coin1.getPercentChange24h()));
                    currentVariation = Variation.Weekly;
                    break;

                case Weekly:    // Show last hour variations sorted by % desc
                    var = getContext().getString(R.string.weekly);
                    Collections.sort(coins, (coin1, coin2) -> Double.valueOf(coin2.getPercentChange7d()).compareTo(coin1.getPercentChange7d()));
                    currentVariation = Variation.Hour;
                    break;
            }
            listAdapter.notifyDataSetChanged();
            tvVariation.setText(String.format(getContext().getString(R.string.percentage_variation_title), var));

        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialogManager.open(getContext());
    }

    //TODO fixare costanti
    public void loadCoinList(String currency, String limit) throws IOException {

        this.coinDataService.getList(currency, limit).enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {

                if (response.isSuccessful()) {

                    List<Coin> body = response.body();
                    if (null != body) {
                        for (Coin c : body) {
                            if (!coins.contains(c)) coins.add(c);
                            else {
                                coins.remove(c);    // Remove coin with old information
                                coins.add(c);       // Add coin with newest information
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                    }

                    ProgressDialogManager.close();

                    if (pullDown != null)
                        pullDown.setRefreshing(false);

                } else {
                    ProgressDialogManager.close();
                    if (pullDown != null)
                        pullDown.setRefreshing(false);
                    int statusCode = response.code();
                    Log.e("ERROR_CODE", String.valueOf(statusCode));
                    ToastManager.create(getContext(), getResources().getString(R.string.coins_request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
                ProgressDialogManager.close();
                if (pullDown != null)
                    pullDown.setRefreshing(false);
                ToastManager.create(getContext(), getResources().getString(R.string.coins_request_error));
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }

    public void showChart(String data) {
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        intent.putExtra(ARG_PRICE_DATA, data);
        startActivity(intent);
    }

}
