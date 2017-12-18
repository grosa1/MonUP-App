package giovanni.tradingtoolkit.marketprices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.model.Coin;
import giovanni.tradingtoolkit.data.remote.ApiUtils;
import giovanni.tradingtoolkit.data.remote.CryptoCompareApiService;
import giovanni.tradingtoolkit.main.ProgressDialogManager;
import giovanni.tradingtoolkit.main.ToastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoinsFragment extends Fragment {

    private static final String ARG_COINS = "COINS";
    private CoinsListAdapter listAdapter;
    private CryptoCompareApiService restService;
    private String coinTypes;

    public CoinsFragment() {
    }

    public static CoinsFragment newInstance(String coins) {
        CoinsFragment fragment = new CoinsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COINS, coins);
        fragment.setArguments(args);
        return fragment;
    }

    public void setCoinTypes(String coinTypes) {
        this.coinTypes = coinTypes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restService = ApiUtils.getRestService();

        if (getArguments() != null) {
            //TODO:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.coins_list_fragment, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

            listAdapter = new CoinsListAdapter(getContext(), new ArrayList<Coin>(0), new CoinsListAdapter.CoinItemListener() {
                @Override
                public void onCoinClick(double priceBtc) {
                    ToastManager.create(getContext(), "Ƀ = " + String.valueOf(priceBtc));
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(listAdapter);
        }

        //String list = "42,365,404,611,808,888,1337,2015,BTC,LTC,DASH,XMR,NXT,ETC,DOGE,ZEC,BTS,DGB,XRP,BTCD,PPC,CRAIG,XBS,XPY,PRC,YBC,DANK,GIVE,KOBO,DT,CETI,SUP,XPD,GEO,CHASH,SPR,NXTI,WOLF,XDP,ACOIN,AERO,ALF,AGS,AMC,ALN,APEX,ARCH,ARG,ARI,AUR,AXR,BCX,BEN,BET,BITB,BLU,BQC,XMY,MOON,ZET,SXC,QTL,ENRG,QRK,RIC,DGC,LIMX,BTB,CAIx,BTG*,BTM,BUK,CACH,CANN,CAP,CAT1,CBX,CCN,CIN,CINNI,CXC,CLAM,CLR,CMC,CNC,CNL,COMM,COOL,CRACK,CRC,CRYPT,CSC,DEM,DMD,XVG,DRKC,DSB,DVC,EAC,EFL,ELC,EMC2,EMD,EXCL,EXE,EZC,FLAP,FC2,FFC,FIBRE,FRC,FRK,FRAC,FST,FTC,GDC,GLC,GLD,GLX,GLYPH,GML,GUE,HAL,HUC,HVC,HYP,ICB,IFC,IOC,JBS,JKC,JUDGE,KDC,KEY*,KGC,LAB*,LGD*,LK7,LKY,LSD,LTB,LTCD,LTCX,LXC,LYC,MAX,MED,MIN,MINT,MN,MNC,MRY,MYST*,NAN,NAUT,NAV,NBL,NEC,NET,NMB,NRB,NOBL,NRS,NVC,NMC,NYAN,OPAL,ORB,OSC,PHS,POINTS,POT,PSEUD,PTS*,PXC,PYC,RDD,RIPO,RT2,RYC,RZR,SAT2,SBC,SDC,SFR,SHADE,SHLD,SILK,SLG,SMC,SOLE,SPA,SPT,SRC,SSV,XLM,SUPER,SWIFT,SYNC,SYS,TAG,TAK,TES,TIT,TOR,TRC,TTC,ULTC,UNB,UNO,URO,USDE,UTIL,VDO,VIA,VOOT,VRC,VTC,WC,WDC,XAI,XBOT,XC,XCASH,XCR,XJO,XLB,XPM,XST,XXX,YAC,ZCC,ZED,ZRC*,BCN,EKN,XDN,XAU,TMC,XEM,NBT,SJCX,START,HUGE,XCP,MAID,007,NSR,MONA,CELL,TEK,TRON,BAY,NTRN,SLING,XVC,CRAVE,XSI,GHS,BYC,GRC,GEMZ,KTK,HZ,FAIR,QORA,NLG,RBY,PTC,KORE,WBB,SSD,XTC,NOTE,GRID*,FLO,MMXIV,8BIT,STV,EBS,AM,XMG,AMBER,JPC,NKT,J,GHC,DTC,ABY,LDOGE,MTR,TRI,SWARM,BBR,BTCRY,BCR,XPB,XDQ,FLDC,SLR,SMAC,TRK,U,UIS,CYP,UFO,ASN,OC,GSM,FSC2,NXTTY,QBK,BLC,MARYJ,OMC,GIG,BITS,LTBC,NEOS,HYPER,VTR,METAL,PINK,GRE,XG,CHILD,BOOM,MINE,ROS,UNAT,SLM,GAIA,TRUST,FCN,XCN,CURE,GMC,MMC,XBC,CYC,OCTO,MSC,EGG,GSX,CAM,RBR,XQN,ICASH,NODE,SOON,BTMI,EVENT,1CR,VIOR,XCO,VMC,MRS,VIRAL,EQM,ISL,QSLV,XWT,XNA,RDN,SKB,BSTY,FCS,GAM,NXS,CESC,TWLV,EAGS,MWC,ADC,MARS,XMS,SIGU,BTX*,DCC,M1,DB,CTO,EDGE,LUX*,FUTC,GLOBE,TAM,MRP,CREVA,XFC,NANAS,LOG,XCE,ACP,DRZ,BUCKS*,BSC,DRKT,CIRC,VERSA,EPY,SQL,POLY,PIGGY,CHA,MIL,CRW,GEN,XPH,GRM,QTZ,ARB,LTS,SPC,GP,BITZ,DUB,GRAV,BOB,MCN,QCN,HEDG,SONG,XSEED,CRE,AXIOM,SMLY,RBT,CHIP,SPEC,GRAM,UNC,SPRTS,ZNY,BTQ,PKB,STR*,SNRG,GHOUL,HNC,DIGS,EXP,GCR,MAPC,MI,CON_,NEU,TX,GRS,SC,CLV,FCT,LYB,BST,PXI,CPC,AMS,OBITS,CLUB,RADS,EMC,BLITZ,HIRE*,EGC,MND,I0C,BTA,KARM,DCR,NAS2,PAK,CRB,DOGED,REP,OK,VOX,AMP,HODL,DGD,EDRC,LSK,WAVES,HTC,GAME,DSH,DBIC,XHI,SPOTS,BIOS,KNC*,CAB,DIEM,GBT,SAR,RCX,PWR,TRUMP,PRM,BCY,RBIES,STEEM,BLRY,XWC,DOT,SCOT,DNET,BAC,XID*,GRT,TCR,POST,INFX,ETHS,PXL,NUM,SOUL,ION,GROW,UNITY,OLDSF,SSTC,NETC,GPU,TAGR,HMP,ADZ,MYC,IVZ,VTA,SLS,SOIL,CUBE,YOC,COIN*,VPRC,APC,STEPS,DBTC,UNIT,AEON,SIB,ERC,AIB,PRIME,BERN,BIGUP,KR,XRE,MEME,XDB,ANTI,BRK,COLX,MNM,ADCN,ZEIT,2GIVE,CGA,SWING,SAFEX,NEBU,AEC,ADN,PULSE,N7,CYG,LGBTQ,UTH,MPRO,KAT,SPM,MOJO,BELA,FLX,BOLI,CLUD,DIME,FLY,HVCO,GIZ,GREXIT,CARBON,DEUR,TUR,LEMON,STS,DISK,NEVA,CYT,FUZZ,NKC,SCRT,XRA,XNX,STAR*,STHR,DBG,WMC,GOTX,FLVR,SHREK,STA*,RISE,REV,PBC,OBS,EXIT,EDC,CLINT,CKC,VIP,NXE,ZOOM,DRACO,YOVI,ORLY,KUBO,INCP,SAK,EVIL,OMA,COX,BNT,EKO,BSD,DES,BIT16,PDC,CMT,CHESS,REE,LQD,MARV,VEC2,OMNI,GSY,TKN*,LIR,MMNXT,SCRPT,LBC,SPX,SBD,CJ,PUT,KRAK,DLISK,IBANK,STRAT,VOYA,ENTER,WGC,BM,PSY,XT,RUST,NZC,SNGLS,XAUR,BFX,UNIQ,CRX,DCT,XPOKE,MUDRA,CNMT,PIZZA,LC,HEAT,ICN,EXB,WINGS,CDX,RBIT,DCS,KMD,GB,ANC,SYNX,MC,EDR,JWL,WAY,TAB,TRIG,BITCNY,BITUSD,ATM,STO,SNS,FSN,CTC,TOT,BTD,BOTS,MDC,FTP,ZET2,COV*,KRB,TELL,ENE,TDFB,BLOCKPAY,BXT,ZYD,GOON,VLT,ZNE,DCK,COVAL,DGDC,TODAY,VRM,ROOT,1ST,GPL,DOPE,B3,FX,PIO,SMSR,UBIQ,ARM,RING,ERB,LAZ,FONZ,BTCR,DROP,PNK,MOOND,DLC,SEN,SCN,WEX,LTH,BRONZ,SH,BUZZ,MG,PSI,XPO,NLC,PSB,XBTS,FIT,PINKX,FIRE,UNF,SPORT,PPY,NTC,EGO,RCN*,X2,MT,TIA,GBRC,XUP,BBCC,EMIGR,BHC,CRAFT,INV,OLYMP,DPAY,ATOM,HKG,ANTC,JOBS,DGORE,THC,TRA,RMS,FJC,VAPOR,SDP,RRT,XZC,PRE,CALC,LEA,CF,CRNK,CFC,VTY,SFE,ARDR,BS,JIF,CRAB,AIR*,HILL,FOREX,MONETA,EC,RUBIT,HCC,BRAIN,VTX,KRC,ROYAL,LFC,ZUR,NUBIS,TENNET,PEC,GMX,32BIT,GNJ,TEAM,SCT,LANA,ELE,GCC,AND,GBYTE,EQUAL,SWEET,2BACCO,DKC,COC,CHOOF,CSH,ZCL,RYCN,PCS,NBIT,WINE,DAR,ARK,ZECD,ZXT,WASH,TESLA,LUCKY,VSL,TPG,LEO,MDT,CBD,PEX,INSANE,GNT,PEN,BASH,FAME,LIV,SP,MEGA,VRS,ALC,DOGETH,KLC,HUSH,BTLC,DRM8,FIST,EBZ,DRS,FGZ,BOSON,ATX,PNC,BRDD,TIME,BIP,XNC,EMB,BTTF,DLR,CSMIC,FIRST,SCASH,XEN,JIO,IW,JNS,TRICK,DCRE,FRE,NPC,PLNC,DGMS,ARCO,KURT,XCRE,ENT,UR,MTLM3,ODNT,EUC,CCX,BCF,SEEDS,POSW,TKS,BCCOIN,SHOR";
        loadCurrenciesList();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialogManager.open(getContext());
    }

    public void loadCurrenciesList() {

        this.restService.getCurrenciesList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    String res = response.body().get("Data").toString();
                    Log.i("LOG", "getCurrencies success");
                    JSONObject coinsJson = new JSONObject(res);

                    int i = 0;
                    List<String> coinsListItems = new ArrayList<>();
                    String buffer = new String();
                    Iterator jsonIterator = coinsJson.keys();
                    while (jsonIterator.hasNext()) {
                        i++;
                        String key = (String) jsonIterator.next();
                        JSONObject j = (JSONObject) coinsJson.get(key);
                        String name = j.get("Name").toString();
                        buffer = buffer + name + ",";

                        if (i == 60 || !jsonIterator.hasNext()) {
                            buffer = buffer.replaceAll("\\*", "");
                            buffer = buffer.substring(0, buffer.length() - 1);
                            coinsListItems.add(buffer);
                            buffer = new String();
                            i = 0;
                        }
                    }

                    for(String s : coinsListItems) {
                        Log.i("LOG", s);
                        loadPricesList(s);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void loadPricesList(String crypto) throws IOException {
        String priceType = "EUR,USD,BTC";

        this.restService.getPricesList(crypto, priceType).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    String res = response.body().get("RAW").toString();
                    //Log.i("RES", res);

                    JSONObject prices = new JSONObject(res);
                    Iterator i = prices.keys();

                    Gson g = new Gson();
                    List<Coin> coins = new ArrayList<>();

                    while (i.hasNext()) {
                        String key = (String) i.next();
                        Coin c = g.fromJson(prices.get(key).toString(), Coin.class);
                        coins.add(c);
                        //Log.i("OBJ", prices.get(key).toString());
                    }

                    if (response.isSuccessful()) {
                        listAdapter.updateCoinsList(coins);
                        Log.i("LOG", "getPriceList success");
                    } else {
                        int statusCode = response.code();
                        Log.e("ERROR_CODE", String.valueOf(statusCode));
                    }

                    ProgressDialogManager.close();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressDialogManager.close();
                ToastManager.create(getContext(), "Errore, riprova più tardi");
                Log.e("REQUEST_ERROR", t.toString());
            }
        });
    }
}
