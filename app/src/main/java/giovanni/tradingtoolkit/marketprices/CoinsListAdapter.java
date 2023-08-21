package giovanni.tradingtoolkit.marketprices;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.marketprices.remote.model.coin_response.Coin;
import giovanni.tradingtoolkit.main.ResourcesLoader;

public class CoinsListAdapter extends RecyclerView.Adapter<CoinsListAdapter.ViewHolder> {

    private List<Coin> coins;
    private Context context;
    private CoinItemListener itemListener;

    public CoinsListAdapter(Context context, List<Coin> coins, CoinItemListener itemListener) {
        this.coins = coins;
        this.context = context;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public CoinsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coin_list_item, parent, false);
        return new ViewHolder(view, this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinsListAdapter.ViewHolder holder, int position) {
        Coin coin = coins.get(position);
        Drawable drawable;

        try {
            drawable = ResourcesLoader.getDrawable(context, coin.getSymbol().toLowerCase());
        } catch (Resources.NotFoundException e) {
            Log.d("RES_ERROR", "Icon not found, set default");
            drawable = ResourcesLoader.getDrawableFromId(context, R.drawable.trading_toolkit);
        }
        holder.coinIcon.setImageDrawable(drawable);

        holder.coinPosition.setText(String.format("%s", coin.getRank()));
        holder.coinName.setText(String.format("%s (%s)", coin.getName(), coin.getSymbol()));

        if (CoinsFragment.currency.equals(CoinsFragment.CURRENCY_EUR)) {
            holder.coinPrice.setText(String.format(Locale.getDefault(), "%.2f €", roundToDecimalPlaces(coin.getPriceEur(), 2)));
            holder.coinPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else if (CoinsFragment.currency.equals(CoinsFragment.CURRENCY_BTC)) {
            holder.coinPrice.setText(String.format(Locale.getDefault(), "%.6f B", roundToDecimalPlaces(coin.getPriceBtc(), 6)));
            holder.coinPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        } else {
            holder.coinPrice.setText(String.format(Locale.getDefault(), "%.2f $", roundToDecimalPlaces(coin.getPriceUsd(), 2)));
            holder.coinPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        // Set percentage 1 hour
        Double coinPercentChange1h = coin.getPercentChange1h();
        int colorId;
        String percentage1h;
        if (coinPercentChange1h < 0) {
            colorId = R.color.materialRed;
            percentage1h = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(coinPercentChange1h), "%");
        } else {
            colorId = R.color.materialGreen;
            percentage1h = String.format(Locale.getDefault(), "+ %.2f%s", coinPercentChange1h, "%");
        }
        holder.percentageVariation1h.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation1h.setText(percentage1h);

        // Set percentage 1 day
        Double coinPercentChange24h = coin.getPercentChange24h();
        String percentage24h;
        if (coinPercentChange24h < 0) {
            colorId = R.color.materialRed;
            percentage24h = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(coinPercentChange24h), "%");
        } else {
            colorId = R.color.materialGreen;
            percentage24h = String.format(Locale.getDefault(), "+ %.2f%s", coinPercentChange24h, "%");
        }
        holder.percentageVariation1d.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation1d.setText(percentage24h);


        // Set percentage 1 week
        Double coinPercentChange7d = coin.getPercentChange7d();
        String percentage7d;
        if (coinPercentChange7d < 0) {
            colorId = R.color.materialRed;
            percentage7d = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(coinPercentChange7d), "%");
        } else {
            colorId = R.color.materialGreen;
            percentage7d = String.format(Locale.getDefault(), "+ %.2f%s", coinPercentChange7d, "%");
        }
        holder.percentageVariation1w.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation1w.setText(percentage7d);

        int background;
        if (position % 2 == 0) {
            background = ResourcesLoader.getColorFromId(context, R.color.lightGreyMaterial);
        } else {
            background = ResourcesLoader.getColorFromId(context, R.color.materialWhite);
        }
        holder.listItem.setBackgroundColor(background);

        // Setting Text Color
        colorId = R.color.marketpricesTextColor;
        holder.coinPosition.setTextColor(context.getResources().getColor(colorId));
        holder.coinName.setTextColor(context.getResources().getColor(colorId));
        holder.coinPrice.setTextColor(context.getResources().getColor(colorId));
        holder.percentageVariation1hText.setTextColor(context.getResources().getColor(colorId));
        holder.percentageVariation1dText.setTextColor(context.getResources().getColor(colorId));
        holder.percentageVariation1wText.setTextColor(context.getResources().getColor(colorId));

        colorId = R.color.marketpricesPercentagesTextColor;
        holder.percentageVariation1h.setTextColor(context.getResources().getColor(colorId));
        holder.percentageVariation1d.setTextColor(context.getResources().getColor(colorId));
        holder.percentageVariation1w.setTextColor(context.getResources().getColor(colorId));

    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public void updateCoinsList(List<Coin> items) {
        coins = items;
        notifyDataSetChanged();
    }

    private Coin getItem(int adapterPosition) {
        return coins.get(adapterPosition);
    }

    public interface CoinItemListener {
        void onCoinClick(String priceBtc);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.coin_list_item)
        public LinearLayout listItem;
        @BindView(R.id.list_position)
        public TextView coinPosition;
        @BindView(R.id.coin_name)
        public TextView coinName;
        @BindView(R.id.percentage_variation_1h)
        public TextView percentageVariation1h;
        @BindView(R.id.percentage_variation_1h_text)
        public TextView percentageVariation1hText;
        @BindView(R.id.percentage_variation_1d)
        public TextView percentageVariation1d;
        @BindView(R.id.percentage_variation_1d_text)
        public TextView percentageVariation1dText;
        @BindView(R.id.percentage_variation_1w)
        public TextView percentageVariation1w;
        @BindView(R.id.percentage_variation_1w_text)
        public TextView percentageVariation1wText;
        @BindView(R.id.price)
        public TextView coinPrice;
        @BindView(R.id.icon)
        public ImageView coinIcon;

        public ViewHolder(View itemView, CoinItemListener coinItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemListener = coinItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Coin coin = getItem(getAdapterPosition());
            itemListener.onCoinClick(String.valueOf(coin.getSymbol()));
            notifyDataSetChanged();
        }
    }

    private Double roundToDecimalPlaces(Double value, int decimalPlaces) {
        Double shift = Math.pow(10, decimalPlaces);
        return Math.round(value * shift) / shift;
    }

    public void filterList (ArrayList<Coin> filteredList) {
        coins = filteredList;
        notifyDataSetChanged();
    }
}


