package giovanni.tradingtoolkit.marketprices;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import giovanni.tradingtoolkit.R;

/**
 * Created by Giovanni on 18/01/2018.
 */

public class ChartMakerView extends MarkerView {
    private TextView tvPrice;
    private TextView tvDate;

    public ChartMakerView(Context context, int layoutResource) {
        super(context, layoutResource);

        // find your layout components
        tvPrice = (TextView) findViewById(R.id.tv_chart_popup_price);
        tvDate = (TextView) findViewById(R.id.tv_chart_popup_time);

    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if(CoinsFragment.currency.equals("EUR")) {
            tvPrice.setText("€ " + e.getY());
        } else if(CoinsFragment.currency.equals("BTC")) {
            tvPrice.setText(String.valueOf(e.getY()));
        } else {
            tvPrice.setText("$ " + e.getY());
        }

        Long fixedDateMills = ChartActivity.timestamp.get((int) e.getX());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvDate.setText(dateFormat.format(new Date(fixedDateMills * 1000)));

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}

