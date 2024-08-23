package giovanni.tradingtoolkit.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import giovanni.tradingtoolkit.R;
import giovanni.tradingtoolkit.main.NullValueException;
import giovanni.tradingtoolkit.main.ToastManager;

public class CalculatorFragment extends Fragment {

    @BindView(R.id.editCurrentUnitPrice)
    EditText currentUnitPrice;
    @BindView(R.id.editExpectedUnitPrice)
    EditText expectedUnitPrice;
    @BindView(R.id.editBudget)
    EditText budget;
    @BindView(R.id.textViewRevenue)
    TextView revenue;
    @BindView(R.id.textTotalGrossResult)
    TextView grossResult;
    @BindView(R.id.textViewCurrentQuantity)
    TextView currentQuantity;
    @BindView(R.id.textViewExpectedQuantity)
    TextView expectedQuantity;
    @BindView(R.id.button)
    Button calculateButton;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, view);

        final RevenueCalculator calc = new RevenueCalculator();

        this.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    calc.setBudget(Float.parseFloat(budget.getText().toString()));
                    calc.setCurrentUnitPrice(Float.parseFloat(currentUnitPrice.getText().toString()));
                    calc.setExpectedUnitPrice(Float.parseFloat(expectedUnitPrice.getText().toString()));

                    String strGrossResult = String.format(Locale.getDefault(), "%.2f", calc.getGrossResult());
                    grossResult.setText(strGrossResult);

                    String strCurrentQt = String.format(Locale.getDefault(), "%.8f", calc.getCurrentQuantity());
                    currentQuantity.setText(strCurrentQt);

                    String strExpectedQt = String.format(Locale.getDefault(), "%.8f", calc.getExpectedQuantity());
                    expectedQuantity.setText(strExpectedQt);

                    if (calc.getRevenue() < 0) {
                        revenue.setBackgroundColor(getResources().getColor(R.color.materialRed));
                    } else {
                        revenue.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }

                    String strRevenue = String.format(Locale.getDefault(), "%.2f", calc.getRevenue());
                    revenue.setText(strRevenue);
                    int colorId = R.color.textColorPrimary;
                    revenue.setTextColor(requireContext().getResources().getColor((colorId)));
                    expectedQuantity.setTextColor(requireContext().getResources().getColor((colorId)));
                    currentQuantity.setTextColor(requireContext().getResources().getColor((colorId)));
                    grossResult.setTextColor(requireContext().getResources().getColor((colorId)));

                } catch (NullValueException | NumberFormatException e) {
                    showValuesError();
                }
            }
        });

        return view;
    }

    private void showValuesError() {
        ToastManager.create(getContext(), R.string.calculator_error);
    }

}