package com.example.toshibapc.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

import ecdsa.ECDSA;
import ecdsa.Point;

/**
 * Created by Acer on 11/30/2016.
 */
public class GenerateKey extends Activity {

    Button generateBtn;
    EditText privateKeyEditText;
    TextView privateKeyTextView;
    TextView publicKeyTextView;
    TextView privateKeyTitle;
    TextView publicKeyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_key);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        generateBtn = (Button) findViewById(R.id.btnGenerate);
        privateKeyEditText = (EditText) findViewById(R.id.editText);
        privateKeyTextView = (TextView) findViewById(R.id.privateKey);
        publicKeyTextView = (TextView) findViewById(R.id.publicKey);
        privateKeyTitle = (TextView) findViewById(R.id.privateKeyTitle);
        publicKeyTitle = (TextView) findViewById(R.id.publicKeyTitle);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                generateKey();
            }
        });
    }

    protected void generateKey() {
        BigInteger priKey = new BigInteger(privateKeyEditText.getText().toString());

        ECDSA ecdsa = new ECDSA();
        Point publicKey = ecdsa.getPublicKey(priKey);

        privateKeyTextView.setText(priKey.toString());
        publicKeyTextView.setText(publicKey.getX().toString() + "\n" + publicKey.getY().toString());

        privateKeyTextView.setVisibility(View.VISIBLE);
        publicKeyTextView.setVisibility(View.VISIBLE);
        privateKeyTitle.setVisibility(View.VISIBLE);
        publicKeyTitle.setVisibility(View.VISIBLE);
    }
}
