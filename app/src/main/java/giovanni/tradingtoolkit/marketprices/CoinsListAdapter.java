package giovanni.tradingtoolkit.marketprices;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.main.DrawableLoader;

/**
 * Created by giiio on 17/12/2017.
 */

public class CoinsListAdapter extends RecyclerView.Adapter<CoinsListAdapter.ViewHolder> {

    private List<Coin> coinList = new ArrayList<>();
    private Context context;
    private CoinItemListener itemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.coin)
        public TextView coinName;
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
            itemListener.onCoinClick(coin.getBtc().getPrice());
            notifyDataSetChanged();
        }

    }

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

    @Override
    public void onBindViewHolder(CoinsListAdapter.ViewHolder holder, int position) {
        Coin coin = coinList.get(position);

        String coinName = coin.getEur().getFromSymbol().toLowerCase();
        Drawable d = DrawableLoader.getDrawable(context, coinName);
        if (d != null) {
            holder.coinIcon.setImageDrawable(d);
        }

        holder.coinName.setText(coin.getEur().getFromSymbol());
        holder.coinPrice.setText(String.format("%.2f", coin.getEur().getPrice()));
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public void updateCoinsList(List<Coin> items) {
        coinList.addAll(items);
        notifyDataSetChanged();
    }

    private Coin getItem(int adapterPosition) {
        return coinList.get(adapterPosition);
    }

    public interface CoinItemListener {
        void onCoinClick(double priceBtc);
    }
}


