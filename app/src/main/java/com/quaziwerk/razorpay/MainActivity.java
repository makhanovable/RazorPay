package com.quaziwerk.razorpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    private EditText input, description;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Checkout.preload(getApplicationContext());

        input = findViewById(R.id.amount);
        description = findViewById(R.id.description);
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");
    }

    public void pay(View view) {
        dialog.show();
        String str = input.getText().toString();
        String desc = description.getText().toString();
        if (!str.isEmpty()) {
            try {
                int amount = Integer.parseInt(str);
                Checkout checkout = new Checkout();
                checkout.setImage(R.drawable.quaziwerk_logo);

                final Activity activity = this;

                try {
                    JSONObject options = new JSONObject();
                    options.put("name", "Quaziwerk LLC");
                    options.put("description", desc);
                    options.put("currency", "INR");
                    options.put("amount", amount);
                    checkout.open(activity, options);
                } catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }

            } catch (Exception exception) {
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        dialog.hide();
        input.setText("");
        description.setText("");
        Toast.makeText(this, "Succesfull, " + s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        dialog.hide();
        Toast.makeText(this, "Error code = " + i + ", " + s, Toast.LENGTH_SHORT).show();
    }

}
