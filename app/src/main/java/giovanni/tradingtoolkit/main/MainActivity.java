package giovanni.tradingtoolkit.main;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.calculator.CalculatorFragment;

public class MainActivity extends AppCompatActivity implements CalculatorFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    CalculatorFragment f = CalculatorFragment.newInstance("str", "str");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f).commit();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}




/*
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.editCurrentUnitPrice)
    EditText currentUnitPrice;
    @BindView(R.id.editExpectedUnitPrice)
    EditText expectedUnitPrice;
    @BindView(R.id.editBudget)
    EditText budget;
    @BindView(R.id.textViewNetGainResult)
    TextView netResult;
    @BindView(R.id.textTotalGrossResult)
    TextView grossResult;
    @BindView(R.id.textViewCurrentQuantity)
    TextView currentQuantity;
    @BindView(R.id.textViewExpectedQuantity)
    TextView expectedQuantity;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //VIEW
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().add(ComingSoonFragment.newInstance(), "DUMMY").commit();
                break;

            case R.id.nav_market_prices:

                break;

            case R.id.nav_market_suggestions:

                break;

            case R.id.nav_tools:

                break;

            case R.id.nav_settings:

                break;

            case R.id.nav_contact:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //TODO PEZZOTTISSIMO, creare classe OPERATIONS in cui inserire tutte le operazioni
    public void convert() {
        float result = 0;

        if (currentUnitPrice.getText().toString().isEmpty()
                || expectedUnitPrice.getText().toString().isEmpty()
                || budget.getText().toString().isEmpty()) {

            Toast toast = Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT);
            View v = toast.getView();
            v.setBackgroundColor(Color.BLACK);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.WHITE);
            toast.show();

        } else {
            float n1 = Float.valueOf(currentUnitPrice.getText().toString());
            float n2 = Float.valueOf(expectedUnitPrice.getText().toString());
            float n3 = Float.valueOf(budget.getText().toString());

            float floCurrentQt = n3 / n1;
            float floGrossresult = floCurrentQt * n2;
            result = floGrossresult - n3;

            float floExpectedQt = n3 / n2;

            String strGrossResult = String.format("%.2f", floGrossresult);
            this.grossResult.setText(strGrossResult);

            String strCurrentQt = String.format("%.8f", floCurrentQt);
            this.currentQuantity.setText(strCurrentQt);

            String strExpectedQt = String.format("%.8f", floExpectedQt);
            this.expectedQuantity.setText(strExpectedQt);
        }

        String formatResult = String.format("%.2f", result);
        if(result < 0) {
            this.netResult.setBackgroundColor(getResources().getColor(R.color.materialRed));
        } else {
            this.netResult.setBackgroundColor(getResources().getColor(R.color.materialGreen));
        }
        this.netResult.setTextColor(Color.WHITE);
        this.netResult.setText(formatResult);
    }

}
*/

