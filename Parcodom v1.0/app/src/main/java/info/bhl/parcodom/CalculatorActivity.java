package info.bhl.parcodom;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Sem on 20-4-2017.
 */

public class CalculatorActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void add(View v) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        TextView result = (TextView) findViewById(R.id.result);
        EditText et1 = (EditText) findViewById(R.id.editText1);

        //get value from edit text box and convert into double
        double InvUren = Double.parseDouble(String.valueOf(et1.getText()));
        RadioButton a1 = (RadioButton) findViewById(R.id.buttonA1);
        RadioButton a2 = (RadioButton) findViewById(R.id.buttonA2);
        RadioButton b1 = (RadioButton) findViewById(R.id.buttonB1);

        //check which radio button is checked
        if (a1.isChecked()) {

            //display conversion
            if ( a1prijsu(InvUren) >= 32.45 ){
                result.setText("€32.45 met een dagkaart, anders € "+ a1prijsu(InvUren));
            }
            else{
                result.setText("€" + a1prijsu(InvUren));
            }

        }
        if (a2.isChecked()) {

            if ( a2prijsu(InvUren) >= 24.96 ){
                result.setText("€24.96 met een dagkaart, anders € "+ a2prijsu(InvUren));
            }
            else{
                result.setText("€" + a2prijsu(InvUren));
            }
        }
        if (b1.isChecked())

            if ( a2prijsu(InvUren) >= 17.82 ){
                result.setText("€17.82 met een dagkaart, anders € "+b1prijsu(InvUren));
            }
            else{
                result.setText("€" + b1prijsu(InvUren));
            }
    }

    //Berekent prijs van Zone A1
    private double a1prijsu(double a1) {
        return (a1 * 4.63);

    }

    //Berekent prijs van Zone A2
    private double a2prijsu(double a2) {
        return (a2 * 3.58);
    }

    //Berekent prijs van Zone B1
    private double b1prijsu(double b1) {
        return (b1 * 2.54);
    }


}
