package giovanni.tradingtoolkit.main;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static final String PREFS_NAME = "STORAGE";
    public static final String STRING_DEFAULT_VALUE = "";
    public static final boolean BOOLEAN_DEFAULT_VALUE = false;

    public static final String KEY_CURRENCY = "CURRENCY";
    public static final String KEY_COINS_CACHE = "CACHE";
    public static final String KEY_NEWS_CACHE = "CACHE_NEWS";
    public static final String KEY_WIDGET_COINS = "COINS_TO_OBSERVE";
    public static final String KEY_WIDGET_ID = "WIDGET_ID";

    public static final String KEY_SETTINGS_DARK_MODE = "SETTINGS_DARK_MODE";

    public static boolean storeString(Context context, String key, String value) {
        boolean result = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(key, value);
            editor.apply();
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
            result = sharedPreferences.getString(key, STRING_DEFAULT_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Boolean storeBoolean(Context context, String key, boolean value) {
        boolean result = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putBoolean(key, value);
            editor.apply();
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean restoreBoolean(Context context, String key) {
        boolean result = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        try {
            result = sharedPreferences.getBoolean(key, BOOLEAN_DEFAULT_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}


