
package giovanni.tradingtoolkit.data.model.coin_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Coin {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("max_supply")
    @Expose
    private Float maxSupply;
    @SerializedName("circulating_supply")
    @Expose
    private Float circulatingSupply;
    @SerializedName("total_supply")
    @Expose
    private Float totalSupply;
    @SerializedName("cmc_rank")
    @Expose
    private Integer cmcRank;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;
    @SerializedName("quote")
    @Expose
    private Quote quote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(Float maxSupply) {
        this.maxSupply = maxSupply;
    }

    public Float getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(Float circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Float getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Float totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Integer getRank() {
        return cmcRank;
    }

    public void setCmcRank(Integer cmcRank) {
        this.cmcRank = cmcRank;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Double getPriceUsd() {
        Double price = this.getQuote().getCurrencyQuote().getPrice();
        return (price != null) ? price : 0.0;
    }

    public Double getPriceEur() {
        Double price = this.getQuote().getCurrencyQuote().getPrice();
        return (price != null) ? price : 0.0;
    }

    public Double getPriceBtc() {
        Double price = this.getQuote().getCurrencyQuote().getPrice();
        return (price != null) ? price : 0.0;
    }

    public Double getPercentChange1h() {
        return this.roundToDecimalPlaces(this.getQuote().getCurrencyQuote().getPercentChange1h(), 2);
    }

    public Double getPercentChange24h() {
        return this.roundToDecimalPlaces(this.getQuote().getCurrencyQuote().getPercentChange24h(), 2);
    }

    public Double getPercentChange7d() {
        return this.roundToDecimalPlaces(this.getQuote().getCurrencyQuote().getPercentChange7d(), 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coin coin = (Coin) o;

        return Objects.equals(id, coin.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Double roundToDecimalPlaces(Double value, int decimalPlaces) {
        Double shift = Math.pow(10, decimalPlaces);
        return Math.round(value * shift) / shift;
    }
}
