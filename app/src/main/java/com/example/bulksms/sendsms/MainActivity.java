package com.example.bulksms.sendsms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bulksms.sendsms.model.InfoRowData;
import com.example.bulksms.sendsms.model.MyCustomAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected TextView smsContent;
    protected Button sendSMS;

    protected ProgressDialog mProgressDialog;

    private Button loadContacts;
    private TextView listContacts;
    private TextView domain;

    private ListView llChb;

    private ArrayList<String> arrData = null;

    private ArrayList<InfoRowData> infodata;

    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrData=new ArrayList<String>();
        infodata = new ArrayList<InfoRowData>();
        //listContacts = (TextView) findViewById(R.id.listContacts);
        loadContacts = (Button) findViewById(R.id.loadContacts);
        smsContent = (TextView) findViewById(R.id.smsContent);
        sendSMS = (Button) findViewById(R.id.sendSMS);
        domain = (TextView) findViewById(R.id.edtdomain);
        loadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrData=new ArrayList<String>();
                infodata = new ArrayList<InfoRowData>();
                loadContacts();
            }
        });

        sendSMS.setEnabled(false);

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            sendSMS.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void loadContacts() {
        StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);

        int i = 0;

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (cursor2.moveToNext()) {
                        String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        builder.append("Contact : ").append(name).append(", Phone Number : ").append(phoneNumber).append("\n\n");
                        arrData.add(phoneNumber);
                        infodata.add(new InfoRowData(false,i));
                        i = i + 1;
                    }
                    cursor2.close();
                }
            }
        }
        cursor.close();

        //listContacts.setText(builder.toString());

        smsContent.setVisibility(View.VISIBLE);
        sendSMS.setVisibility(View.VISIBLE);

        llChb = (ListView) findViewById(R.id.llChb);
        llChb.invalidate();
        llChb.setAdapter(new MyCustomAdapter(MainActivity.this, arrData, infodata, domain.getText().toString(), sendSMS, smsContent));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendSMS.setEnabled(true);
                }
                break;
        }
    }
}

