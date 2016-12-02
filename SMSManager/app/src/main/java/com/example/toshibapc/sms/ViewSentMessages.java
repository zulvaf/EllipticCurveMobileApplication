package com.example.toshibapc.sms;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Acer on 11/30/2016.
 */
public class ViewSentMessages extends Activity implements OnItemClickListener {

    private static ViewSentMessages inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    boolean isEncrypted=false, isSigned=false;

    public static ViewSentMessages instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        smsListView = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setOnItemClickListener(this);

        refreshSmsSent();
    }

    public void refreshSmsSent() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/sent/"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        long timeMillis = smsInboxCursor.getColumnIndex("date");

        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        String dateText = format.format(date);

        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String[] body = smsInboxCursor.getString(indexBody).split("\n");
            String message = "";
            for (int i = 0; i < body.length; ++i) {
                if (i == body.length - 1) {
                    message += body[i];
                } else {
                    message += body[i] + "\n";
                }
            }
            String str = smsInboxCursor.getString(indexAddress) + " at " + dateText +
                    "\n" + message + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        String[] signature = new String[2];
        isEncrypted = false;
        isSigned = false;
        try {
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                if (smsMessages[i].equals("encrypted")) {
                    isEncrypted = true;
                } else if (smsMessages[i].equals("signed")) {
                    isSigned = true;
                    signature[0] = smsMessages[i+2];
                    signature[1] = smsMessages[i+3];
                    i += 4;
                } else if (!smsMessages[i].equals("<ds>") && !smsMessages[i].equals("</ds>")){
                    if (i == smsMessages.length - 1) {
                        smsMessage += smsMessages[i];
                    } else {
                        smsMessage += smsMessages[i] + "\n";
                    }
                }
            }

            Intent intent = new Intent(ViewSentMessages.this, ViewDetails.class);
            intent.putExtra("address", address);
            intent.putExtra("body", smsMessage);
            intent.putExtra("isEncrypted", isEncrypted);
            intent.putExtra("isSigned", isSigned);
            intent.putExtra("siganture", signature);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
