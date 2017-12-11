package giovanni.tradingtoolkit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giiio on 11/12/2017.
 */

//COIN PRICE MAPPING FROM CRYPTOCOMPARE - ENDPOINT EXAMPLE: https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,BCH,BTG,ETH,ETC,XMR,ZEC,DASH,LTC,NXT,BTCD,XRP,PPC,DODGE&tsyms=EUR,USD,BTC

public class CoinPriceResponse {
        @SerializedName("EUR")
        @Expose
        private Double eur;
        @SerializedName("USD")
        @Expose
        private Double usd;
        @SerializedName("BTC")
        @Expose
        private Double btc;

        public Double getEUR() {
            return eur;
        }

        public void setEUR(Double eur) {
            this.eur = eur;
        }

        public Double getUSD() {
            return usd;
        }

        public void setUSD(Double usd) {
            this.usd = usd;
        }

        public Double getBTC() {
            return btc;
        }

        public void setBTC(Double btc) {
            this.btc = btc;
        }
}
