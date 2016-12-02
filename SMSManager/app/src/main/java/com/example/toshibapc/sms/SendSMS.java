package com.example.toshibapc.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.InputType;
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
import miniblockcipher.MiniBlockCipher;
import utils.BitUtils;

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

        if (phoneNo.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter phone number.", Toast.LENGTH_LONG).show();
        } else {
            if (isEncrypt.isChecked()) {
                encryptMessage();
            } else {
                if (isSigned.isChecked()) {
                    signMessage();
                } else {
                    message = header.concat(message);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                }
            }
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

    public void signMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Private Key");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter your private key");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String privateKey = input.getText().toString();

                byte[] str = message.getBytes();
                SHA1 sha = new SHA1(str, message.length()*8);
                BigInteger e = (sha.shaAlgorithm());

                ECDSA ecdsa = new ECDSA();
                String[] signature = ecdsa.buildSignature(e, new BigInteger(privateKey));

                header = header.concat("signed\n");
                header = header.concat("<ds>\n" + signature[0] + "\n" + signature[1] + "\n</ds>\n");

                message = header.concat(message);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
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

    public void encryptMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Encryption Key");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter encryption key");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                header = header.concat("encrypted\n");
                String encryptionKey = input.getText().toString();

                // Encrypt message
                try {
                    byte[] btext = message.getBytes("UTF-8");
                    byte[] bkey = encryptionKey.getBytes("UTF-8");

                    MiniBlockCipher myCipher = new MiniBlockCipher(bkey);
                    byte[] bres = myCipher.encrypt(btext, bkey);

                    message = BitUtils.byteToHex(bres);
                } catch (Exception e) {
                }

                if (isSigned.isChecked()) {
                    signMessage();
                } else {
                    message = header.concat(message);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
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
