package giovanni.tradingtoolkit.marketprices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.data.remote.ApiUtils;
import giovanni.tradingtoolkit.data.remote.CryptoCompareApiService;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.ToastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsFragment extends Fragment {

    private static final String ARG_COINS = "COINS";
    private CoinsListAdapter listAdapter;
    private SwipeRefreshLayout pullDown;
    private CryptoCompareApiService restService;
    private String coinTypes;

    private List<Dummy> names;

    public void setNames(List<Dummy> names) {
        this.names = names;
    }


    public CoinsFragment() {
    }

    public static CoinsFragment newInstance(String coins) {
        CoinsFragment fragment = new CoinsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COINS, coins);
        fragment.setArguments(args);
        return fragment;
    }

    public void setCoinTypes(String coinTypes) {
        this.coinTypes = coinTypes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restService = ApiUtils.getRestService();

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
            public void onCoinClick(double priceBtc) {
                ToastManager.create(getContext(), "Ƀ = " + String.valueOf(priceBtc));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);

        //this.setCoinTypes("BTC,BCH,BTG,ETH,ETC,XMR,ZEC,DASH,LTC,NXT,BTCD,XRP,PPC,DODGE,PPC,");

        loadCurrenciesList();

        pullDown = (SwipeRefreshLayout) view;
        pullDown.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCurrenciesList();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialogManager.open(getContext());
    }

    public void loadCurrenciesList() {

        this.restService.getCurrenciesList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                //todo: CONTROLLO SU CAMPO SUCCESS PEZZOTTO

                try {
                    String res = response.body().get("Data").toString();
                    Log.i("LOG", "getCurrencies success");
                    JSONObject coinsJson = new JSONObject(res);

                    final List<String> img = new ArrayList<>();
                    List<Dummy> coinNames = new ArrayList<>();
                    String buffer = new String();
                    String name;
                    Iterator jsonIterator = coinsJson.keys();
                    while (jsonIterator.hasNext()) {
                        String key = (String) jsonIterator.next();
                        JSONObject j = (JSONObject) coinsJson.get(key);
                        if ((j.getInt("SortOrder")) < 68) {
                            coinNames.add(new Dummy(j.getString("FullName"), j.getInt("SortOrder")));
                            name = j.get("Name").toString();
                            buffer = buffer + name + ",";

                            //Log.d("IMG", "https://www.cryptocompare.com" + j.getString("ImageUrl"));
                        }
                    }

                    buffer = buffer.replaceAll("\\*", "");
                    buffer = buffer.substring(0, buffer.length() - 1);
                    Log.i("BUFF", buffer);
                    setNames(coinNames);
                    loadPricesList(buffer);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void loadPricesList(String crypto) throws IOException {
        final String priceType = "EUR";

        this.restService.getPricesList(crypto, priceType).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    String res = response.body().get("RAW").toString();
                    //Log.i("RES", res);

                    JSONObject prices = new JSONObject(res);
                    Iterator i = prices.keys();

                    Gson g = new Gson();
                    int resId = 0;
                    JSONObject object;
                    List<Coin> coins = new ArrayList<>();

                    while (i.hasNext()) {
                        String key = (String) i.next();
                        object = (JSONObject) prices.get(key);
                        Coin c = g.fromJson(object.get(priceType).toString(), Coin.class);
                        coins.add(c);
                        //Log.i("OBJ", prices.get(key).toString());
                    }

                    for (int index = 0; index < coins.size(); index++) {
                        coins.get(index).setFullName(names.get(index).name);
                        coins.get(index).setSort(names.get(index).sort);
                    }

                    Collections.sort(coins, new Comparator<Coin>() {
                        public int compare(Coin obj1, Coin obj2) {
                            // ## Ascending order
                            return Integer.valueOf(obj1.getSort()).compareTo(obj2.getSort()); // To compare string values
                            // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                            // ## Descending order
                            // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                            // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
                        }
                    });

                    if (response.isSuccessful()) {
                        listAdapter.updateCoinsList(coins);

                        ProgressDialogManager.close();

                        if (pullDown != null)
                            pullDown.setRefreshing(false);

                        Log.i("LOG", "getPriceList success");
                    } else {
                        int statusCode = response.code();
                        Log.e("ERROR_CODE", String.valueOf(statusCode));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressDialogManager.close();
                ToastManager.create(getContext(), "Errore, riprova più tardi");
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }

}
