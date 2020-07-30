package giovanni.tradingtoolkit.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.home_widget.CoinListWidget;
import giovanni.tradingtoolkit.main.SharedPrefs;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        boolean darkModeEnabled = SharedPrefs.restoreBoolean(getBaseContext(), SharedPrefs.KEY_SETTINGS_DARK_MODE);

        Switch colorModeSwitch = findViewById(R.id.color_mode);

        colorModeSwitch.setChecked(darkModeEnabled);

        colorModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i("SETTINGS", "onCheckedChanged: DARK MODE");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                Log.i("SETTINGS", "onCheckedChanged: LIGHT MODE");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            SharedPrefs.storeBoolean(getBaseContext(), SharedPrefs.KEY_SETTINGS_DARK_MODE, isChecked);
            CoinListWidget.updateWidget(getApplicationContext());
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
