package giovanni.tradingtoolkit.marketprices;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import giovanni.tradingtoolkit.data.model.CoinPriceResponse;
import giovanni.tradingtoolkit.marketprices.dummy.DummyContent;
import giovanni.tradingtoolkit.marketprices.dummy.DummyContent.DummyItem;
import giovanni.tradingtoolkit.R;

import java.util.List;


public class MyCoinPriceRecyclerViewAdapter extends RecyclerView.Adapter<MyCoinPriceRecyclerViewAdapter.ViewHolder> {

    private List<DummyItem> mValues;
    private List<CoinPriceResponse> pricesList;

    public MyCoinPriceRecyclerViewAdapter(List<DummyItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_coinprice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


    public void updatePriceList(List<DummyItem> items) {
        mValues = items;
        notifyDataSetChanged();
    }
}
