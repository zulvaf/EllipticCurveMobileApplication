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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Acer on 11/30/2016.
 */
public class GenerateKey extends Activity {

    Button generateBtn;
    EditText privateKey;
    EditText baseValue;
    ArrayAdapter arrayAdapter;
    ArrayList<String> keyList = new ArrayList<String>();
    ListView keyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_key);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        generateBtn = (Button) findViewById(R.id.btnGenerate);
        privateKey = (EditText) findViewById(R.id.editText);
        baseValue = (EditText) findViewById(R.id.editText2);

        keyListView = (ListView) findViewById(R.id.KeyList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, keyList);
        keyListView.setAdapter(arrayAdapter);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                generateKey();
            }
        });
    }

    protected void generateKey() {
        int priKey = Integer.parseInt(privateKey.getText().toString());
        int baseVal = Integer.parseInt(baseValue.getText().toString());

        arrayAdapter.clear();
        String str = "Private key: " + priKey + "\n" + "Public key: ";
        arrayAdapter.add(str);

    }
}
