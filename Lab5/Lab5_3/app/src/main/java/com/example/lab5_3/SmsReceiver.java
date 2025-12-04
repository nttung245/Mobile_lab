package com.example.lab5_3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver"; // Tag để lọc log cho dễ

    public static final String SMS_FORWARD_BROADCAST_RECEIVER = "sms_forward_broadcast_receiver";
    public static final String SMS_MESSAGE_ADDRESS_KEY = "sms_messages_key";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            return; // Không xử lý nếu không phải SMS
        }

        Log.d(TAG, "SMS Received!");

        // 2. Lấy keyword từ string resources
        String queryString = context.getString(R.string.are_you_ok).toLowerCase();

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.e(TAG, "Bundle is null, cannot process SMS.");
            return;
        }

        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus == null) {
            Log.e(TAG, "PDUs are null, cannot process SMS.");
            return;
        }

        HashMap<String, StringBuilder> messageMap = new HashMap<>();
        String format = bundle.getString("format");

        for (Object pdu : pdus) {
            SmsMessage message;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                message = SmsMessage.createFromPdu((byte[]) pdu, format);
            } else {
                // API cũ hơn đã bị deprecated
                message = SmsMessage.createFromPdu((byte[]) pdu);
            }

            String originatingAddress = message.getOriginatingAddress();
            if (originatingAddress == null) {
                continue;
            }

            StringBuilder body = messageMap.get(originatingAddress);
            if (body == null) {
                body = new StringBuilder();
                messageMap.put(originatingAddress, body);
            }
            body.append(message.getMessageBody());
        }

        ArrayList<String> addressesWithKeyword = new ArrayList<>();
        for (String address : messageMap.keySet()) {
            String fullMessageBody = messageMap.get(address).toString().toLowerCase();
            Log.d(TAG, "From: " + address + " | Full Message: " + fullMessageBody);

            if (fullMessageBody.contains(queryString)) {
                Log.d(TAG, "Keyword MATCHED for address: " + address);
                addressesWithKeyword.add(address);
            }
        }

        if (!addressesWithKeyword.isEmpty()) {
            Log.d(TAG, "Found " + addressesWithKeyword.size() + " matching messages. Forwarding to MainActivity.");

            if (MainActivity.isRunning) {

                Log.d(TAG, "MainActivity is running. Posting to LiveData.");
                MainActivity.newRequestersLiveData.postValue(addressesWithKeyword);
            } else {

                Log.d(TAG, "MainActivity is not running. Starting it with new Intent.");
                Intent iMain = new Intent(context, MainActivity.class);
                iMain.putStringArrayListExtra(SMS_MESSAGE_ADDRESS_KEY, addressesWithKeyword);
                iMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iMain);
            }
        }
    }
}
