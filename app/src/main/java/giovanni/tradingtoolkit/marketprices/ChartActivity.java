package giovanni.tradingtoolkit.marketprices;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.HystoPrice;
import giovanni.tradingtoolkit.data.model.HystoPriceResponse;
import giovanni.tradingtoolkit.data.remote.CryptoCompareService;
import giovanni.tradingtoolkit.data.remote.RetrofitClient;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.ResourcesLoader;
import giovanni.tradingtoolkit.main.ToastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.chart_toolbar)
    Toolbar toolbar;
    @BindView(R.id.chart)
    LineChart lineChart;
    @BindView(R.id.tv_7_gg)
    TextView tv7gg;
    @BindView(R.id.tv_30_gg)
    TextView tv30gg;
    @BindView(R.id.tv_90_gg)
    TextView tv90gg;

    private final int WEEK = 7;
    private final int MONTH = 30;
    private final int REQUEST_DAYS = 90;
    private CryptoCompareService hystoService;
    private List<Entry> weekEntry;
    private List<Entry> monthEntry;
    private List<Entry> fullEntry;

    public static List<Long> timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        String coin = getIntent().getStringExtra(CoinsFragment.ARG_PRICE_DATA);
        toolbar.setTitle(coin);
        getSupportActionBar().setTitle(coin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.hystoService = RetrofitClient.getCryptoCompareService();

        //CHART SETTINGS
        lineChart.setTouchEnabled(true);
        lineChart.setMarker(new ChartMakerView(this, R.layout.chart_popup));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setLabelCount(5, true);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setDrawBorders(false);
        Description desc = new Description();
        desc.setText(coin + "/EUR");
        desc.setTextSize(13);
        lineChart.setDescription(desc);
        lineChart.setNoDataText(getResources().getString(R.string.chart_data_error));

        ProgressDialogManager.open(this);

        //TODO fixare costanti
        String coinCurrency = CoinsFragment.DEFAULT_CURRENCY;
        loadData(coin, coinCurrency, String.valueOf(REQUEST_DAYS));

        tv7gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataset(weekEntry);
            }
        });

        tv30gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataset(monthEntry);
            }
        });

        tv90gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataset(fullEntry);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setDataset(List<Entry> entries) {
        //DATASET SETTINGS
        String labelDesc = getResources().getString(R.string.chart_price_label);
        LineDataSet dataset = new LineDataSet(entries, labelDesc + " " + "EUR");
        dataset.setValueTextSize(13);
        dataset.setDrawValues(false);
        dataset.setHighlightEnabled(true);
        dataset.setDrawFilled(true);
        dataset.setColor(Color.BLUE);
        dataset.setHighLightColor(ResourcesLoader.getColorFromId(this, R.color.colorAccent));

        LineData data = new LineData(dataset);
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.animateX(1500);
    }

    //TODO è brutto
    public void setChartData(List<HystoPrice> prices) {
        this.weekEntry = new ArrayList<>();
        this.monthEntry = new ArrayList<>();
        this.fullEntry = new ArrayList<>();
        this.timestamp = new ArrayList<>();

        ArrayList<Entry> entries = new ArrayList<>();
        int size = prices.size();
        for (int i = 0; i < size; i++) {
            entries.add(new Entry(i, prices.get(i).getClose().floatValue()));
            this.timestamp.add(prices.get(i).getTime());

            if(size - i <= WEEK)
                this.weekEntry.add(entries.get(i));

            if(size - i <= MONTH)
                this.monthEntry.add(entries.get(i));
        }

        this.fullEntry = entries;
    }

    public void loadData(String coinSym, String currency, String daysCount) {
        hystoService.getList(coinSym, currency, daysCount).enqueue(new Callback<HystoPriceResponse>() {
            @Override
            public void onResponse(Call<HystoPriceResponse> call, Response<HystoPriceResponse> response) {

                if(response.body() != null) {
                    String responseStatus = response.body().getResponse();
                    if (responseStatus.equals("Success")) {
                        setChartData(response.body().getData());
                        setDataset(weekEntry);
                    } else {
                        showError();
                    }
                } else {
                    showError();
                }

                ProgressDialogManager.close();
            }

            @Override
            public void onFailure(Call<HystoPriceResponse> call, Throwable t) {
                ProgressDialogManager.close();
                showError();
            }
        });
    }

    private void showError() {
        ToastManager.create(this, getResources().getString(R.string.chart_data_error));
    }

}
