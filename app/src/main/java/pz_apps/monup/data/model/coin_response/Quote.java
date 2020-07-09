
package pz_apps.monup.data.model.coin_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName(value="USD", alternate={"EUR", "BTC"})
    @Expose
    private Price currencyQuote;

    public Price getCurrencyQuote() {
        return currencyQuote;
    }

    public void setCurrencyQuote(Price currencyQuote) {
        this.currencyQuote = currencyQuote;
    }
}
