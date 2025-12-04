package com.example.lab5_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

public class PowerStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) return;

        if (Objects.equals(intent.getAction(), Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, context.getString(R.string.power_connected), Toast.LENGTH_LONG).show();
        }

        if (Objects.equals(intent.getAction(), Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, context.getString(R.string.power_disconnected), Toast.LENGTH_LONG).show();
        }
    }
}
