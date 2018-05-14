package giovanni.tradingtoolkit.marketprices;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.ResourcesLoader;

public class CoinsListAdapter extends RecyclerView.Adapter<CoinsListAdapter.ViewHolder> {

    private List<Coin> coins;
    private Context context;
    private CoinItemListener itemListener;

    CoinsListAdapter(Context context, List<Coin> coins, CoinItemListener itemListener) {
        this.coins = coins;
        this.context = context;
        this.itemListener = itemListener;
    }

    @Override
    public CoinsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coin_list_item, parent, false);
        return new ViewHolder(view, this.itemListener);
    }

    @Override
    public void onBindViewHolder(CoinsListAdapter.ViewHolder holder, int position) {
        Coin coin = coins.get(position);
        Drawable drawable;

        try {
            drawable = ResourcesLoader.getDrawable(context, coin.getSymbol().toLowerCase());
        } catch (Resources.NotFoundException e) {
            Log.e("RES_ERROR", "Icon not found, set default");
            drawable = ResourcesLoader.getDrawableFromId(context, R.drawable.ic_no_image);
        }
        holder.coinIcon.setImageDrawable(drawable);

        holder.coinPosition.setText(String.format("%s", String.valueOf(coin.getRank())));
        holder.coinName.setText(String.format("%s (%s)", coin.getName(), coin.getSymbol()));

        if (CoinsFragment.currency.equals("EUR")) {
            holder.coinPrice.setText(String.format(Locale.getDefault(), "%.2f €", roundToDecimalPlaces(coin.getPriceEur(), 2)));
            holder.coinPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else if (CoinsFragment.currency.equals("BTC")) {
            holder.coinPrice.setText(coin.getPriceBtc());
            holder.coinPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        } else {
            holder.coinPrice.setText(String.format(Locale.getDefault(), "%.2f $", roundToDecimalPlaces(coin.getPriceUsd(), 2)));
            holder.coinPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        // Set percentage 1 hour
        Double variation = coin.getPercentChange1h();
        int colorId;
        String perc;
        if (variation < 0) {
            colorId = R.color.materialRed;
            perc = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(variation), "%");
        } else {
            colorId = R.color.materialGreen;
            perc = String.format(Locale.getDefault(), "+ %.2f%s", variation, "%");
        }
        holder.percentageVariation1h.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation1h.setText(perc);
        holder.percentageVariation1h.setTextColor(Color.WHITE);

        // Set percentage 1 day
        variation = coin.getPercentChange24h();
        if (variation < 0) {
            colorId = R.color.materialRed;
            perc = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(variation), "%");
        } else {
            colorId = R.color.materialGreen;
            perc = String.format(Locale.getDefault(), "+ %.2f%s", variation, "%");
        }
        holder.percentageVariation1d.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation1d.setText(perc);
        holder.percentageVariation1d.setTextColor(Color.WHITE);


        // Set percentage 1 week
        variation = coin.getPercentChange7d();
        if (variation < 0) {
            colorId = R.color.materialRed;
            perc = String.format(Locale.getDefault(), "- %.2f%s", Math.abs(variation), "%");
        } else {
            colorId = R.color.materialGreen;
            perc = String.format(Locale.getDefault(), "+ %.2f%s", variation, "%");
        }
        holder.percentageVariation1w.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation1w.setText(perc);
        holder.percentageVariation1w.setTextColor(Color.WHITE);

        int background;
        if (position % 2 == 0) {
            background = ResourcesLoader.getColorFromId(context, R.color.lightGreyMaterial);
        } else {
            background = ResourcesLoader.getColorFromId(context, R.color.materialWhite);
        }
        holder.listItem.setBackgroundColor(background);

    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    void updateCoinsList(List<Coin> items) {
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
        public TextView percentageVariation1d;
        @BindView(R.id.percentage_variation_1d)
        public TextView percentageVariation1w;
        @BindView(R.id.percentage_variation_1w)
        public TextView percentageVariation1h;
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

//    private Double computePercentageVariation(Double openPrice, Double currentPrice) {
//        Double result = 0d;
//        if(openPrice != null && currentPrice != null) {
//            result = ((currentPrice - openPrice)/currentPrice)*100;
//            //result = roundToDecimalPlaces(result, 2);
//        }
//        return result;
//    }

    private Double roundToDecimalPlaces(Double value, int decimalPlaces) {
        Double shift = Math.pow(10, decimalPlaces);
        return Math.round(value * shift) / shift;
    }
}


