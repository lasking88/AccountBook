package com.ohbrothers.www.accountbook.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.ohbrothers.www.accountbook.R;
import com.ohbrothers.www.accountbook.model.DataLab;
import com.ohbrothers.www.accountbook.model.InOutcome;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jk on 5/21/17.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BroadcastReceiver Received");
        mContext = context;

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Object[] messages = (Object[])bundle.get("pdus");
            SmsMessage[] smsMessage = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            String message = smsMessage[0].getMessageBody().toString();
            if (message.indexOf(context.getString(R.string.extract_condition)) != -1)
                parsingSMS(message);
            Log.d(TAG, "SMS Message: " + message);
        }
    }

    private void parsingSMS(String text) {
        List<String> parts = Arrays.asList(text.split("\n"));

        String cost = "";
        for(int i = 0; i < parts.size()-1; ++i) {
            Log.d(TAG, parts.get(i));
            if (parts.get(i).indexOf(mContext.getString(R.string.unit)) != -1)
                cost = parsingCost(parts.get(i));
        }
        Log.d(TAG, "cost : " + cost);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long currentTimeMillis = System.currentTimeMillis();
        String key = sdf.format(new Date(currentTimeMillis));
        if (cost != "") {
            InOutcome ioc = new InOutcome(-Integer.valueOf(cost), parts.get(parts.size()-1));
            DataLab.get(mContext).addData(key, ioc);
            Toast.makeText(mContext, R.string.added_a_inoutcome, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, R.string.cannot_add_inoutcome, Toast.LENGTH_SHORT).show();
        }

    }

    private String parsingCost(String cost) {
        String pureCost = "";
        for (char c : cost.toCharArray()) {
            if (Character.isDigit(c)) pureCost += c;
        }
        return pureCost;
    }

}

