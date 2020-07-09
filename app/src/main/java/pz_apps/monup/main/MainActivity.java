package pz_apps.monup.main;

import android.app.ActivityManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pz_apps.monup.calculator.CalculatorFragment;
import pz_apps.monup.news.NewsFragment;
import pz_apps.monup.settings.AboutActivity;
import pz_apps.monup.R;
import pz_apps.monup.data.remote.LoadCoinReceiver;
import pz_apps.monup.data.remote.LoadCoinService;
import pz_apps.monup.data.remote.LoadNewsReceiver;
import pz_apps.monup.data.remote.LoadNewsService;
import pz_apps.monup.home_widget.CoinListWidgetConfigureActivity;
import pz_apps.monup.marketprices.CoinsFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_marketplace_black_24dp,
            R.drawable.ic_tools_black_24dp,
            R.drawable.ic_news
    };

    Intent mServiceIntent;
    LoadCoinService CoinService;
    LoadNewsService NewsService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        this.loadCoins();
        this.loadNews();
    }

    public void loadCoins() { //TODO: make static
        CoinService = new LoadCoinService();
        mServiceIntent = new Intent(MainActivity.this, CoinService.getClass());
        if (!isLoadServiceRunning(CoinService.getClass())) {
            Intent i = new Intent(this, LoadCoinReceiver.class);
            this.sendBroadcast(i);
        }
    }

    public void loadNews() { //TODO: make static
        NewsService = new LoadNewsService();
        mServiceIntent = new Intent(MainActivity.this, NewsService.getClass());
        if (!isLoadServiceRunning(NewsService.getClass())) {
            Intent i = new Intent(this, LoadNewsReceiver.class);
            this.sendBroadcast(i);
        }
    }

    private boolean isLoadServiceRunning(Class<?> serviceClass) { //TODO: make static
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isLoadCoinServiceRunn?", true + "");
                return true;
            }
        }
        Log.i("isLoadCoinServiceRunn?", false + "");
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.configure_widget: {
                // TODO integrate with CoinListWidgetConfigureActivity
                String widgetId = SharedPrefs.restoreString(this, SharedPrefs.KEY_WIDGET_ID);

                if (!widgetId.isEmpty()) {
                    Intent configIntent = new Intent(this, CoinListWidgetConfigureActivity.class);
                    configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Integer.parseInt(widgetId));
                    startActivity(configIntent);
                } else {
                    ToastManager.create(this, R.string.no_active_widget);
                }
                return true;
            }

            case R.id.reset_widget: {
                // TODO integrate with CoinListWidgetConfigureActivity
                String widgetId = SharedPrefs.restoreString(this, SharedPrefs.KEY_WIDGET_ID);

                if (!widgetId.isEmpty()) {
                    AppWidgetHost host = new AppWidgetHost(this, 1);
                    host.deleteAppWidgetId(Integer.parseInt(widgetId));

                    SharedPrefs.storeString(this, SharedPrefs.KEY_WIDGET_COINS, null);
                    SharedPrefs.storeString(this, SharedPrefs.KEY_WIDGET_ID, null);
                    ToastManager.create(this, R.string.widget_resetted);
                } else {
                    ToastManager.create(this, R.string.no_active_widget);
                }

                return true;
            }

            case R.id.about: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onPause() {
//
//        Intent intent = new Intent(MainActivity.this, LoadCoinService.class);
//        startService(intent);
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        Intent intent = new Intent(MainActivity.this, LoadCoinService.class);
//        startService(intent);
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//
//        Intent intent = new Intent(MainActivity.this, LoadCoinService.class);
//        stopService(intent);
//        super.onResume();
//    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(CoinsFragment.newInstance(), "");
        adapter.addFrag(CalculatorFragment.newInstance(), "");
        adapter.addFrag(NewsFragment.newInstance(), "");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}





