package giovanni.tradingtoolkit.news;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.data.remote.LoadCoinReceiver;
import giovanni.tradingtoolkit.data.remote.LoadNewsReceiver;
import giovanni.tradingtoolkit.data.remote.LoadNewsService;
import giovanni.tradingtoolkit.main.ToastManager;
import io.cryptocontrol.cryptonewsapi.models.Article;

public class NewsFragment extends Fragment {

    @BindView(R.id.news_list)
    ListView listView;
    @BindView(R.id.refresh_news)
    Button btnRefresh;
    @BindView(R.id.text_news)
    TextView textNews;

    private Context context;

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ArrayAdapter<Article> newsAdapter = new NewsAdapter();

        //listView.setAdapter(R.id.news_list, intent);
        //runService(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        context = this.getContext();

        btnRefresh.setOnClickListener(v -> {
            Log.e("API-RES", "Refresh");
            runService(context);
        });

        return view;
    }

    private void showValuesError() {
        ToastManager.create(getContext(), getResources().getString(R.string.calculator_error));
    }

    private void showDialog() {

        AlertDialog.Builder builder1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            builder1 = new AlertDialog.Builder(getContext());
        }
        if (builder1 != null) {
            builder1.setMessage("Refresh News");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void runService(Context context) {
        Intent i = new Intent(context, LoadNewsReceiver.class);
        context.sendBroadcast(i);
    }
}
