package giovanni.tradingtoolkit.main;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static final String PREFS_NAME = "STORAGE";
    public static final String DEFAULT_VALUE = "";

    public static final String KEY_CURRENCY = "CURRENCY";
    public static final String KEY_COINS_CACHE = "CACHE";
    public static final String KEY_WIDGET_COINS = "COINS_TO_OBSERVE";
    public static final String KEY_WIDGET_ID = "WIDGET_ID";

    public static Boolean storeString(Context context, String key, String value) {
        Boolean result = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(key, value);
            editor.commit();
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String restoreString(Context context, String key) {
        String result = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        try {
            result = sharedPreferences.getString(key, DEFAULT_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}


