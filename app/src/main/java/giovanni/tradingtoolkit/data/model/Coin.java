package giovanni.tradingtoolkit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giiio on 17/12/2017.
 */

public class Coin {
    //    @SerializedName("TYPE")
//    @Expose
//    private String type;
//    @SerializedName("MARKET")
//    @Expose
//    private String market;
    @SerializedName("FROMSYMBOL")
    @Expose
    private String fromSymbol;
//    @SerializedName("TOSYMBOL")
//    @Expose
//    private String toSymbol;
    //    @SerializedName("FLAGS")
//    @Expose
//    private String fLAGS;
    @SerializedName("PRICE")
    @Expose
    private Double price;
    //    @SerializedName("LASTUPDATE")
//    @Expose
//    private Integer lASTUPDATE;
//    @SerializedName("LASTVOLUME")
//    @Expose
//    private Double lASTVOLUME;
//    @SerializedName("LASTVOLUMETO")
//    @Expose
//    private Double lASTVOLUMETO;
//    @SerializedName("LASTTRADEID")
//    @Expose
//    private String lASTTRADEID;
//    @SerializedName("VOLUMEDAY")
//    @Expose
//    private Double vOLUMEDAY;
//    @SerializedName("VOLUMEDAYTO")
//    @Expose
//    private Double vOLUMEDAYTO;
//    @SerializedName("VOLUME24HOUR")
//    @Expose
//    private Double vOLUME24HOUR;
//    @SerializedName("VOLUME24HOURTO")
//    @Expose
//    private Double vOLUME24HOURTO;
//    @SerializedName("OPENDAY")
//    @Expose
//    private Double oPENDAY;
//    @SerializedName("HIGHDAY")
//    @Expose
//    private Double hIGHDAY;
//    @SerializedName("LOWDAY")
//    @Expose
//    private Double lOWDAY;
    @SerializedName("OPEN24HOUR")
    @Expose
    private Double open24Hour;
    @SerializedName("HIGH24HOUR")
    @Expose
    private Double high24Hour;
    @SerializedName("LOW24HOUR")
    @Expose
    private Double low24Hour;
//    @SerializedName("LASTMARKET")
//    @Expose
//    private String lASTMARKET;
//    @SerializedName("CHANGE24HOUR")
//    @Expose
//    private Double cHANGE24HOUR;
//    @SerializedName("CHANGEPCT24HOUR")
//    @Expose
//    private Double cHANGEPCT24HOUR;
//    @SerializedName("CHANGEDAY")
//    @Expose
//    private Double cHANGEDAY;
//    @SerializedName("CHANGEPCTDAY")
//    @Expose
//    private Double cHANGEPCTDAY;
//    @SerializedName("SUPPLY")
//    @Expose
//    private Double sUPPLY;
//    @SerializedName("MKTCAP")
//    @Expose
//    private Double mktcap;
//    @SerializedName("TOTALVOLUME24H")
//    @Expose
//    private Double totalvolume24H;
//    @SerializedName("TOTALVOLUME24HTO")
//    @Expose
//    private Double totalvolume24Hto;

    private String fullName;
    private int sort;

    public Coin(String fromSymbol, Double price, Double open24Hour, Double high24Hour, Double low24Hour) {
        this.fromSymbol = fromSymbol;
        this.price = price;
        this.open24Hour = open24Hour;
        this.high24Hour = high24Hour;
        this.low24Hour = low24Hour;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getFromSymbol() {
        return fromSymbol;
    }

    public Double getPrice() {
        return price;
    }

    public Double getOpen24Hour() {
        return open24Hour;
    }

    public Double getHigh24Hour() {
        return high24Hour;
    }

    public Double getLow24Hour() {
        return low24Hour;
    }
}
