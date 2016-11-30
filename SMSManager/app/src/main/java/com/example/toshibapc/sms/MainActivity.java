package com.example.toshibapc.sms;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        btn = (Button) findViewById(R.id.btnInbox);
        btn.setTextColor(Color.WHITE);
        btn = (Button) findViewById(R.id.btnGenerator);
        btn.setTextColor(Color.WHITE);
        btn = (Button) findViewById(R.id.btnOutbox);
        btn.setTextColor(Color.WHITE);
        btn = (Button) findViewById(R.id.btnCompose);
        btn.setTextColor(Color.WHITE);
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
