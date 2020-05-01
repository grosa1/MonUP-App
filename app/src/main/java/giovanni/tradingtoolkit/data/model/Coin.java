
package giovanni.tradingtoolkit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
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
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("num_market_pairs")
    @Expose
    private Integer numMarketPairs;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("max_supply")
    @Expose
    private Integer maxSupply;
    @SerializedName("circulating_supply")
    @Expose
    private Integer circulatingSupply;
    @SerializedName("total_supply")
    @Expose
    private Integer totalSupply;
    @SerializedName("platform")
    @Expose
    private String platform;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getNumMarketPairs() {
        return numMarketPairs;
    }

    public void setNumMarketPairs(Integer numMarketPairs) {
        this.numMarketPairs = numMarketPairs;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(Integer maxSupply) {
        this.maxSupply = maxSupply;
    }

    public Integer getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(Integer circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Integer getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Integer totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public Object getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Double getPriceUsd() {
        return quote.getUSD().getPrice();
    }

    public Double getPriceEur() {
        return quote.getUSD().getPrice();
    } //TODO: find a method to calculate them

    public String getPriceBtc() {
        return (quote.getUSD().getPrice()).toString();
    }//TODO: find a method to calculate them


    public Double getPercentChange1h() {
        String percentChange1h = "0.00"; //TODO: find a method to calculate them

        return null != percentChange1h ? Double.parseDouble(percentChange1h) : 0D;
    }

    public Double getPercentChange24h() {
        String percentChange24h = "0.00"; //TODO: find a method to calculate them

        return null != percentChange24h ? Double.parseDouble(percentChange24h) : 0D;
    }

    public Double getPercentChange7d() {
        String percentChange7d = "0.00"; //TODO: find a method to calculate them

        return null != percentChange7d ? Double.parseDouble(percentChange7d) : 0D;
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
}
