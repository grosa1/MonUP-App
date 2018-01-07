package giovanni.tradingtoolkit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giiio on 17/12/2017.
 */

public class Coin {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("rank")
    @Expose
    private int rank;
    @SerializedName("price_usd")
    @Expose
    private Double priceUsd;
    @SerializedName("price_btc")
    @Expose
    private String priceBtc;
    @SerializedName("24h_volume_usd")
    @Expose
    private String _24hVolumeUsd;
    @SerializedName("market_cap_usd")
    @Expose
    private String marketCapUsd;
    @SerializedName("available_supply")
    @Expose
    private String availableSupply;
    @SerializedName("total_supply")
    @Expose
    private String totalSupply;
    @SerializedName("max_supply")
    @Expose
    private String maxSupply;
    @SerializedName("percent_change_1h")
    @Expose
    private String percentChange1h;
    @SerializedName("percent_change_24h")
    @Expose
    private String percentChange24h;
    @SerializedName("percent_change_7d")
    @Expose
    private String percentChange7d;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;
    @SerializedName("price_eur")
    @Expose
    private Double priceEur;
    @SerializedName("24h_volume_eur")
    @Expose
    private String _24hVolumeEur;
    @SerializedName("market_cap_eur")
    @Expose
    private String marketCapEur;

    public Coin(String id,
                String name,
                String symbol,
                int rank,
                Double priceUsd,
                String priceBtc,
                String _24hVolumeUsd,
                String marketCapUsd,
                String availableSupply,
                String totalSupply,
                String maxSupply,
                String percentChange1h,
                String percentChange24h,
                String percentChange7d,
                String lastUpdated,
                Double priceEur,
                String _24hVolumeEur,
                String marketCapEur) {

        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.priceUsd = priceUsd;
        this.priceBtc = priceBtc;
        this._24hVolumeUsd = _24hVolumeUsd;
        this.marketCapUsd = marketCapUsd;
        this.availableSupply = availableSupply;
        this.totalSupply = totalSupply;
        this.maxSupply = maxSupply;
        this.percentChange1h = percentChange1h;
        this.percentChange24h = percentChange24h;
        this.percentChange7d = percentChange7d;
        this.lastUpdated = lastUpdated;
        this.priceEur = priceEur;
        this._24hVolumeEur = _24hVolumeEur;
        this.marketCapEur = marketCapEur;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getRank() {
        return rank;
    }

    public Double getPriceUsd() {
        return priceUsd;
    }

    public String getPriceBtc() {
        return priceBtc;
    }

    public String get_24hVolumeUsd() {
        return _24hVolumeUsd;
    }

    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public String getMaxSupply() {
        return maxSupply;
    }

    public double getPercentChange1h() {
        return null != percentChange1h ? Double.parseDouble(percentChange1h) : 0D;
    }

    public double getPercentChange24h() {
        return null != percentChange24h ?  Double.parseDouble(percentChange24h) : 0D;
    }

    public double getPercentChange7d() {
        return null != percentChange7d ?  Double.parseDouble(percentChange7d) : 0D;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public Double getPriceEur() {
        return priceEur;
    }

    public String get_24hVolumeEur() {
        return _24hVolumeEur;
    }

    public String getMarketCapEur() {
        return marketCapEur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coin coin = (Coin) o;

        return id != null ? id.equals(coin.id) : coin.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
