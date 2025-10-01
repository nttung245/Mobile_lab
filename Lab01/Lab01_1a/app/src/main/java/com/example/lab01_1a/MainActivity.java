package com.example.lab01_1a;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");

        // Parent vertical layout
        LinearLayout llParent = new LinearLayout(this);
        llParent.setOrientation(LinearLayout.VERTICAL);
        llParent.setPadding(40, 260, 40, 40); // push down so not hidden by status bar
        llParent.setGravity(Gravity.START);
        llParent.setBackgroundColor(0xFFEFEFEF);

        // Name row
        LinearLayout llName = new LinearLayout(this);
        llName.setOrientation(LinearLayout.HORIZONTAL);
        llName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView tvNameLabel = new TextView(this);
        tvNameLabel.setText("Name: ");
        tvNameLabel.setTextSize(20f);
        tvNameLabel.setPadding(0, 0, 8, 0);
        tvNameLabel.setTextColor(0xFF000000); // black
        llName.addView(tvNameLabel);

        TextView tvNameValue = new TextView(this);
        tvNameValue.setText("John Doe");
        tvNameValue.setTextSize(20f);
        tvNameValue.setTextColor(0xFF000000);
        llName.addView(tvNameValue);

        // Address row
        LinearLayout llAddress = new LinearLayout(this);
        llAddress.setOrientation(LinearLayout.HORIZONTAL);
        llAddress.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView tvAddrLabel = new TextView(this);
        tvAddrLabel.setText("Address: ");
        tvAddrLabel.setTextSize(20f);
        tvAddrLabel.setPadding(0, 20, 8, 0);
        tvAddrLabel.setTextColor(0xFF000000);
        llAddress.addView(tvAddrLabel);

        TextView tvAddrValue = new TextView(this);
        tvAddrValue.setText("911 Hollywood Blvd");
        tvAddrValue.setTextSize(20f);
        tvAddrValue.setTextColor(0xFF000000);
        llAddress.addView(tvAddrValue);

        // Add rows to parent
        llParent.addView(llName);
        llParent.addView(llAddress);

        // Set content view
        setContentView(llParent);
    }
}
