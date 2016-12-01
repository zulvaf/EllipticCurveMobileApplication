package com.example.toshibapc.sms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Acer on 12/1/2016.
 */
public class ViewDetails extends Activity {
    Button btn;
    String message;
    String address;
    TextView textAddress, textBody;
    boolean isEncrypted=false, isSigned=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        textAddress = (TextView) findViewById(R.id.textViewAddress);
        textAddress.setText(address);
        textAddress.setTypeface(null, Typeface.BOLD);

        message = intent.getStringExtra("body");
        textBody = (TextView) findViewById(R.id.textViewBody);
        textBody.setText(message);

        btn = (Button) findViewById(R.id.btnDecrypt);
        btn.setTextColor(Color.WHITE);

        isEncrypted = intent.getBooleanExtra("isEncrpted", false);
        isSigned = intent.getBooleanExtra("isSigned", false);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if (isEncrypted) {
                    //@TODO: DECRYPT
                    Toast.makeText(getApplicationContext(), "Please enter phone number.", Toast.LENGTH_LONG).show();
                    textBody.setText("hoooo"); //set text hasil decrypt
                    btn.setEnabled(false);
                    isEncrypted = false;
                //}
            }

        });
    }
}
