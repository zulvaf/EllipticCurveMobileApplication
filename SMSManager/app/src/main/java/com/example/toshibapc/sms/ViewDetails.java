package com.example.toshibapc.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

import ecdsa.ECDSA;
import ecdsa.Point;
import ecdsa.SHA1;

import static android.widget.Toast.*;

/**
 * Created by Acer on 12/1/2016.
 */
public class ViewDetails extends Activity {
    Button btnDecrypt, btnVerify;
    String message;
    String address;
    TextView textAddress, textBody;
    boolean isEncrypted=false, isSigned=false;
    String decryptionKey = "";
    String signedKeyX, signedKeyY;

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

        isEncrypted = intent.getExtras().getBoolean("isEncrypted");
        isSigned = intent.getExtras().getBoolean("isSigned");

        message = intent.getStringExtra("body");
        textBody = (TextView) findViewById(R.id.textViewBody);
        if (isSigned) {
            textBody.setText(message + "\n\n<ds>\n" + getIntent().getStringArrayExtra("siganture")[0] + "\n" + getIntent().getStringArrayExtra("siganture")[1] + "\n</ds>");
        } else {
            textBody.setText(message);
        }

        btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
        btnDecrypt.setTextColor(Color.WHITE);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        btnVerify.setTextColor(Color.WHITE);

        addListenerOnButton();
        if (!isEncrypted) {
            btnDecrypt.setEnabled(false);
            btnDecrypt.setVisibility(View.GONE);
        }
        if (!isSigned) {
            btnVerify.setEnabled(false);
            btnVerify.setVisibility(View.GONE);
        }
    }

    public void addListenerOnButton() {
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decryptMessage();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignature();

            }
        });
    }

    public void decryptMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Decryption Key");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter key for decryption");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //@TODO: DECRYPT
                decryptionKey = input.getText().toString();
                textBody.setText("hoooo"); //set text hasil decrypt
                isEncrypted = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void verifySignature() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Signed Key");

        // Set up the layout
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);
        final EditText inputX = new EditText(this);
        final EditText inputY = new EditText(this);
        inputX.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputY.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputX.setHint("Enter x value of sender's key");
        inputY.setHint("Enter y value of sender's key");
        layout.addView(inputX);
        layout.addView(inputY);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signedKeyX = inputX.getText().toString();
                signedKeyY = inputY.getText().toString();

                ECDSA ecdsa = new ECDSA();
                Point publicKey = new Point(new BigInteger(signedKeyX), new BigInteger(signedKeyY));
                BigInteger[] signature = new BigInteger[2];
                signature[0] = new BigInteger(getIntent().getStringArrayExtra("siganture")[0]);
                signature[1] = new BigInteger(getIntent().getStringArrayExtra("siganture")[1]);
                // hash message to e
                byte[] str = message.getBytes();
                SHA1 sha = new SHA1(str, message.length()*8);
                BigInteger e = (sha.shaAlgorithm());

                if (ecdsa.verifySignature(signature, e, publicKey)) {
                    Toast.makeText(getApplicationContext(), "Signature verified", LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Signature not verified", LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
