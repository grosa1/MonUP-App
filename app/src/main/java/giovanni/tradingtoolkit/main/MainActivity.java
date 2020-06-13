package giovanni.tradingtoolkit.main;

import android.app.ActivityManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.calculator.CalculatorFragment;
import giovanni.tradingtoolkit.data.remote.LoadCoinReceiver;
import giovanni.tradingtoolkit.data.remote.LoadCoinService;
import giovanni.tradingtoolkit.data.remote.LoadNewsReceiver;
import giovanni.tradingtoolkit.data.remote.LoadNewsService;
import giovanni.tradingtoolkit.home_widget.CoinListWidgetConfigureActivity;
import giovanni.tradingtoolkit.marketprices.CoinsFragment;
import giovanni.tradingtoolkit.news.NewsFragment;
import giovanni.tradingtoolkit.settings.AboutActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private int[] tabIcons = {
            //R.drawable.ic_notifications_black_24dp,
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
        //viewPager.setCurrentItem(1);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //        1514505600
//        int t = (int) e.getX();
//        tvDate.setText();
//        Timestamp tr = ChartActivity.timestamp.get(t);
//        1517176991155
//        String str = "1514505600000";
//                        1516752000000
//        Date d = new Date(Long.valueOf(str));
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Log.d("TIME", ChartActivity.timestamp.get(t).toString());
//                Log.d("TSTAMP", dateFormat.format(d));


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
//            case R.id.update:
//                return true;
//
//            case R.id.settings:
//                return true;

            case R.id.configure_widget: {
                // TODO integrate with CoinListWidgetConfigureActivity
                String widgetId = SharedPrefs.restoreString(this, SharedPrefs.KEY_WIDGET_ID);

                if (!widgetId.isEmpty()) {
                    Intent configIntent = new Intent(this, CoinListWidgetConfigureActivity.class);
                    configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Integer.parseInt(widgetId));
                    startActivity(configIntent);
                } else {
                    Toast.makeText(this, R.string.no_active_widget, Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            case R.id.reset_widget: {
                // TODO integrate with CoinListWidgetConfigureActivity
                String widgetId = SharedPrefs.restoreString(this, SharedPrefs.KEY_WIDGET_ID);
                AppWidgetHost host = new AppWidgetHost(this, 1);
                host.deleteAppWidgetId(Integer.parseInt(widgetId));

                SharedPrefs.storeString(this, SharedPrefs.KEY_WIDGET_COINS, null);
                SharedPrefs.storeString(this, SharedPrefs.KEY_WIDGET_ID, null);
                Toast.makeText(this, R.string.widget_resetted, Toast.LENGTH_SHORT).show();
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
        //adapter.addFrag(NotificationsFragment.newInstance(), "Notifications");
        //adapter.addFrag(CoinsFragment.newInstance(""), "Market prices");
        //adapter.addFrag(CalculatorFragment.newInstance(), "Tools");

//        adapter.addFrag(CoinsFragment.newInstance(), getResources().getString(R.string.coins));
//        adapter.addFrag(CalculatorFragment.newInstance(), getResources().getString(R.string.tools));

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





