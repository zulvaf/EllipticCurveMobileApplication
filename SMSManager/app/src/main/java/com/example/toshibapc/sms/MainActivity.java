package com.example.toshibapc.sms;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToInbox(View view) {
        Intent intent = new Intent(MainActivity.this, ReceiveSms.class);
        startActivity(intent);
    }

    public void goToCompose(View view) {
        Intent intent = new Intent(this, SendSMS.class);
        startActivity(intent);
    }

    public void goToKeyGenerator(View view) {
        Intent intent = new Intent(MainActivity.this, GenerateKey.class);
        startActivity(intent);
    }

    public void goToOutbox(View view) {
        Intent intent = new Intent(MainActivity.this, ViewSentMessages.class);
        startActivity(intent);
    }
}
