package giovanni.tradingtoolkit.marketprices;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.data.remote.ApiService;
import giovanni.tradingtoolkit.data.remote.RetrofitClient;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.ToastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsFragment extends Fragment {

    private static final String ARG_COINS = "COINS";
    private static final String currency = "EUR";
    private static final String limit = "200";

    private CoinsListAdapter listAdapter;
    private SwipeRefreshLayout pullDown;
    private ApiService restService;

    public CoinsFragment() {
    }

    public static CoinsFragment newInstance(String coins) {
        CoinsFragment fragment = new CoinsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COINS, coins);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restService = RetrofitClient.getRestService();

        if (getArguments() != null) {
            //TODO:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.coins_list_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        listAdapter = new CoinsListAdapter(getContext(), new ArrayList<Coin>(0), new CoinsListAdapter.CoinItemListener() {
            @Override
            public void onCoinClick(String priceBtc) {
                ToastManager.create(getContext(), "Ƀ = " + priceBtc);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);

        try {
            loadCoinList(currency, limit);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pullDown = (SwipeRefreshLayout) view;
        pullDown.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    loadCoinList(currency, limit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialogManager.open(getContext());
    }

    public void loadCoinList(String currency, String limit) throws IOException {

        this.restService.getList(currency, limit).enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {

                if (response.isSuccessful()) {
                    List<Coin> coins = response.body();
                    listAdapter.updateCoinsList(coins);
                    Log.d("RES", response.body().get(1).getSymbol());

                    ProgressDialogManager.close();

                    if (pullDown != null)
                        pullDown.setRefreshing(false);

                    Log.i("LOG", "getPriceList success");
                } else {
                    ProgressDialogManager.close();
                    if (pullDown != null)
                        pullDown.setRefreshing(false);
                    int statusCode = response.code();
                    Log.e("ERROR_CODE", String.valueOf(statusCode));
                    ToastManager.create(getContext(), "Errore: riprova più tadi");

                }
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
                ProgressDialogManager.close();
                if (pullDown != null)
                    pullDown.setRefreshing(false);
                ToastManager.create(getContext(), "Errore, riprova più tardi");
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }

}
