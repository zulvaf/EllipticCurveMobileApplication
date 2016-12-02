package com.example.toshibapc.sms;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ecdsa.ECDSA;
import ecdsa.SHA1;

/**
 * Created by Acer on 11/30/2016.
 */
public class SendSMS extends Activity {
    //private static SendSMS inst;
    private ToggleButton isEncrypt, isSigned;
    private String header = new String();
    private Button sendBtn;
    private EditText txtPhoneNo;
    private EditText txtMessage;
    private String phoneNo;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        Button btn = (Button) findViewById(R.id.btnSendSMS);
        btn.setTextColor(Color.WHITE);

        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        addListenerOnButton();

        /*sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
                Intent intent = new Intent(SendSMS.this, ReceiveSms.class);
                startActivity(intent);
            }
        });*/
    }

    protected void sendSMSMessage() {
        phoneNo = txtPhoneNo.getText().toString();
        message = txtMessage.getText().toString();
        isEncrypt = (ToggleButton) findViewById(R.id.toggleEncrypt);
        isSigned = (ToggleButton) findViewById(R.id.toggleDigSign);
        String header = new String();

        if (phoneNo.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter phone number.", Toast.LENGTH_LONG).show();
        } else {
            if (isEncrypt.isChecked()) {
                //@TODO: encrypt message
                header = header.concat("encrypted\n");
            }
            if (isSigned.isChecked()) {
                //@TODO: get private key
                byte[] str = message.getBytes();
                SHA1 sha = new SHA1(str, message.length()*8);
                BigInteger e = (sha.shaAlgorithm());

                ECDSA ecdsa = new ECDSA();
                BigInteger[] signature = ecdsa.buildSignature(e, new BigInteger("123"));

                header = header.concat("signed\n");
                header = header.concat("<ds>\n" + signature[0] + "\n" + signature[1] + "\n</ds>\n");
            }

            message = header.concat(message);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }
    }

    public void addListenerOnButton() {
        sendBtn = (Button) findViewById(R.id.btnSendSMS);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }

        });
    }
}
