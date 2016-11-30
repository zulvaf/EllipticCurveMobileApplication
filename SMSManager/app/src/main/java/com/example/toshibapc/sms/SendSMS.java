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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Acer on 11/30/2016.
 */
public class SendSMS extends Activity {
    //private static SendSMS inst;
    private ToggleButton isEncrypt, isSigned;
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

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
    }

    public void addListenerOnButton() {

        isEncrypt = (ToggleButton) findViewById(R.id.toggleEncrypt);
        isSigned = (ToggleButton) findViewById(R.id.toggleDigSign);
        sendBtn = (Button) findViewById(R.id.btnSendSMS);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                result.append("isEncrypt? : ").append(isEncrypt.getText());
                result.append("\nisSigned : ").append(isSigned.getText());

                Toast.makeText(SendSMS.this, result.toString(),
                        Toast.LENGTH_SHORT).show();

                sendSMSMessage();
                Intent intent = new Intent(SendSMS.this, ReceiveSms.class);
                startActivity(intent);

            }

        });
    }
}
