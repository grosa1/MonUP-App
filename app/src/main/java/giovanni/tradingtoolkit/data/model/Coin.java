package giovanni.tradingtoolkit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giiio on 17/12/2017.
 */

public class Coin {
    @SerializedName("BTC")
    @Expose
    private BTC btc;
    @SerializedName("USD")
    @Expose
    private USD usd;
    @SerializedName("EUR")
    @Expose
    private EUR eur;

    public Coin(BTC btc, USD usd, EUR eur) {
        this.btc = btc;
        this.usd = usd;
        this.eur = eur;
    }

    public BTC getBtc() {
        return btc;
    }

    public USD getUsd() {
        return usd;
    }

    public EUR getEur() {
        return eur;
    }
}
