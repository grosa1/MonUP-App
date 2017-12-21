package giovanni.tradingtoolkit.marketprices;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.ResourcesLoader;

public class CoinsListAdapter extends RecyclerView.Adapter<CoinsListAdapter.ViewHolder> {

    private List<Coin> coinList = new ArrayList<>();
    private Context context;
    private CoinItemListener itemListener;

    //ADAPTER
    public CoinsListAdapter(Context context, List<Coin> coins, CoinItemListener itemListener) {
        coinList = coins;
        this.context = context;
        this.itemListener = itemListener;
    }

    @Override
    public CoinsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.coin_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, this.itemListener);
        return viewHolder;
    }

    //VIEW HOLDER
    @Override
    public void onBindViewHolder(CoinsListAdapter.ViewHolder holder, int position) {
        Coin coin = coinList.get(position);
        Drawable drawable;

        try {
            drawable = ResourcesLoader.getDrawable(context, coin.getSymbol().toLowerCase());
        } catch (Resources.NotFoundException e) {
            Log.e("RES_ERROR", "Icon not found, set default");
            drawable = ResourcesLoader.getDrawableFromId(context, R.drawable.ic_no_image);
        }
        holder.coinIcon.setImageDrawable(drawable);

        holder.coinPosition.setText(String.valueOf(position + 1) + ".");
        holder.coinName.setText(coin.getName() + " (" + coin.getSymbol() + ")");

        Double price = coin.getPriceEur();
        holder.coinPrice.setText(String.format("%.2f", roundToDecimalPlaces(price, 2)) + " €");

        Double priceStatus = Double.parseDouble(coin.getPercentChange24h());
        int colorId;
        String perc;
        if(priceStatus < 0) {
            colorId = R.color.materialRed;
            perc = String.format("%.2f", priceStatus) + "%";
        } else {
            colorId = R.color.materialGreen;
            perc = "+" + String.format("%.2f", priceStatus) + "%";
        }
        holder.percentageVariation.setBackgroundColor(context.getResources().getColor(colorId));
        holder.percentageVariation.setText(perc);
        holder.percentageVariation.setTextColor(Color.WHITE);

        int background;
        if(position%2 == 0) {
            background = ResourcesLoader.getColorFromId(context, R.color.lightGreyMaterial);
        } else {
            background = ResourcesLoader.getColorFromId(context, R.color.materialWhite);
        }
        holder.listItem.setBackgroundColor(background);

    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public void updateCoinsList(List<Coin> items) {
        coinList = items;
        notifyDataSetChanged();
    }

    private Coin getItem(int adapterPosition) {
        return coinList.get(adapterPosition);
    }

    public interface CoinItemListener {
        void onCoinClick(String priceBtc);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.list_item)
        public RelativeLayout listItem;
        @BindView(R.id.list_position)
        public TextView coinPosition;
        @BindView(R.id.coin_name)
        public TextView coinName;
        @BindView(R.id.percentage_variation)
        public TextView percentageVariation;
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
            itemListener.onCoinClick(String.valueOf(coin.getPriceBtc()));
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
        Double shift = Math.pow(10,decimalPlaces);
        return Math.round(value*shift)/shift;
    }
}


