package com.example.bulksms.sendsms.model;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bulksms.sendsms.MainActivity;
import com.example.bulksms.sendsms.R;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {

    public ArrayList<String> mdata;
    public Context mContext;
    public ArrayList<InfoRowData> mInfoData;
    public Button mSendSMS;
    protected ProgressDialog pd;
    public TextView mSMSContent;
    public String mdomain;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;

    public MyCustomAdapter(final Context context, ArrayList<String> data, ArrayList<InfoRowData> infoData, String domain, Button sendSMS, final TextView smsContent) {
        mContext = context;
        mdata = data;
        mInfoData = infoData;
        mSendSMS = sendSMS;
        mSMSContent = smsContent;
        mdomain = domain;

        mSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = ProgressDialog.show(mContext, "Progress Dialog", "Loading..", true,true);
                String msg = smsContent.getText().toString();
                int count = 0;
                for (int i = 0; i < mInfoData.size(); i++) {
                    if (mInfoData.get(i).isclicked) {

                        count++;
                        //String phoneNumber = mInfoData.get(i).toString();
                        String phoneNumber = mdata.get(i).toString();

                        if (!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(phoneNumber)) {
                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                SmsManager smsManager = SmsManager.getDefault();
                                msg = msg + "\n" + mdomain;
                                smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
                                pd.dismiss();
                                Toast.makeText(mContext, "SEND SMS SUCCESS", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        } else {
                            Toast.makeText(mContext, "Enter a message and a phone number", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }
                if (count == 0) {
                    Toast.makeText(mContext, "Select phone number", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(mContext, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = null;
        row = View.inflate(mContext, R.layout.item_row, null);
        TextView tvContent = (TextView) row.findViewById(R.id.tvContent);
        //tvContent.setText(data[position]);
        tvContent.setText(mdata.get(position));
        //System.out.println("The Text is here like.. == "+tvContent.getText().toString());

        final CheckBox cb = (CheckBox) row
                .findViewById(R.id.chbContent);
        cb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mInfoData.get(position).isclicked) {
                    mInfoData.get(position).isclicked = false;
                } else {
                    mInfoData.get(position).isclicked = true;
                }

                for (int i = 0; i < mInfoData.size(); i++) {
                    if (mInfoData.get(i).isclicked) {
                        System.out.println("Selectes Are == " + mdata.get(i));
                    }
                }
            }
        });

        if (mInfoData.get(position).isclicked) {

            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        return row;
    }

}