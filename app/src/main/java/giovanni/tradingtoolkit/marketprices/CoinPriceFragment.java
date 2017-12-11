package giovanni.tradingtoolkit.marketprices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.CoinPriceResponse;
import giovanni.tradingtoolkit.data.remote.ApiUtils;
import giovanni.tradingtoolkit.data.remote.CryptoCompareApiService;
import giovanni.tradingtoolkit.marketprices.dummy.DummyContent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinPriceFragment extends Fragment {

    public static CoinPriceFragment newInstance() {
        CoinPriceFragment fragment = new CoinPriceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coinprice_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyCoinPriceRecyclerViewAdapter(DummyContent.ITEMS));
        }

        try {
            this.loadPricesList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void loadPricesList() throws IOException {
        String coinType = "BTC,BCH,BTG,ETH,ETC,XMR,ZEC,DASH,LTC,NXT,BTCD,XRP,PPC,DODGE";
        String priceType = "EUR,USD,BTC";
        CryptoCompareApiService cryptoCompareService;
        cryptoCompareService = ApiUtils.getPricesListService();
        Call<List<CoinPriceResponse>> call = cryptoCompareService.getPricesList(coinType, priceType);
        List<CoinPriceResponse> result = call.execute().body();
        Log.i("RES", result.toString());
    }

    private void showErrorMessage() {
        Toast toast = Toast.makeText(getContext(), "Errore, riprova più tardi", Toast.LENGTH_SHORT);
        toast.show();
    }
}
