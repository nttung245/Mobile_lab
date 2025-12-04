package com.example.lab5_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    // MutableLiveData sẽ giữ chuỗi nội dung tin nhắn SMS.
    private static MutableLiveData<String> smsContentLiveData = new MutableLiveData<>();

    // Phương thức này giờ sẽ cập nhật LiveData thay vì gửi broadcast
    private void processReceive(Context context, Intent intent) {
        Toast.makeText(context, getString(R.string.you_have_a_new_message), Toast.LENGTH_LONG).show();

        Bundle bundle = intent.getExtras();
        if (bundle == null) return;

        Object[] messages = (Object[]) bundle.get("pdus");
        if (messages == null) return;

        StringBuilder smsBuilder = new StringBuilder();

        for (Object msg : messages) {
            SmsMessage smsMsg;
            if (Build.VERSION.SDK_INT >= 23) {
                String format = bundle.getString("format");
                smsMsg = SmsMessage.createFromPdu((byte[]) msg, format);
            } else {
                smsMsg = SmsMessage.createFromPdu((byte[]) msg);
            }

            String smsBody = smsMsg.getMessageBody();
            String address = smsMsg.getDisplayOriginatingAddress();
            smsBuilder.insert(0, address + ":\n" + smsBody + "\n\n");
        }

        // Cập nhật giá trị cho LiveData. Dùng postValue() vì BroadcastReceiver
        // có thể chạy trên một luồng khác.
        smsContentLiveData.postValue(smsBuilder.toString());
    }

    private void initBroadcastReceiver() {
        intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processReceive(context, intent);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvContent = findViewById(R.id.tv_content);

        // Tạo một Observer sẽ được kích hoạt mỗi khi smsContentLiveData có dữ liệu mới.
        final Observer<String> smsObserver = new Observer<String>() {
            @Override
            public void onChanged(String newSmsContent) {
                // Cập nhật TextView với nội dung SMS mới.
                tvContent.setText(newSmsContent);
            }
        };

        // Bắt đầu quan sát LiveData. Dữ liệu sẽ tự động được gửi tới smsObserver
        // khi có thay đổi và khi Activity đang ở trạng thái hoạt động.
        smsContentLiveData.observe(this, smsObserver);


        // Các phần còn lại giữ nguyên
        checkAndRequestSmsPermission();
        initBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            initBroadcastReceiver();
        }
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void checkAndRequestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied. The app cannot receive messages.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
