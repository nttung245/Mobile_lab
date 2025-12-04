package com.example.lab5_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private ReentrantLock reentrantLock;
    private SwitchCompat swAutoResponse;
    private LinearLayout llButtons;
    private Button btnSafe, btnMayday;
    private ArrayList<String> requesters;
    private ArrayAdapter<String> adapter;
    private ListView lvMessages;

    public static boolean isRunning;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private final String AUTO_RESPONSE = "auto_response";

    // LiveData sẽ chứa một ArrayList<String> với các số điện thoại mới.
    // Dùng static để SmsReceiver có thể truy cập được.
    public static MutableLiveData<ArrayList<String>> newRequestersLiveData = new MutableLiveData<>();


    private void findViewsByIds() {
        swAutoResponse = findViewById(R.id.sw_auto_response);
        llButtons = findViewById(R.id.ll_buttons);
        lvMessages = findViewById(R.id.lv_messages);
        btnSafe = findViewById(R.id.btn_safe);
        btnMayday = findViewById(R.id.btn_mayday);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsByIds();
        initVariables();
        handleOnClickListenner();


        final Observer<ArrayList<String>> requestersObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> newAddresses) {
                // Gọi phương thức để xử lý các địa chỉ mới này trên luồng UI.
                processNewAddresses(newAddresses);
            }
        };

        // Bắt đầu quan sát LiveData. Dữ liệu sẽ tự động được gửi tới observer
        // khi có thay đổi và khi Activity đang ở trạng thái hoạt động.
        newRequestersLiveData.observe(this, requestersObserver);

        // Yêu cầu quyền gửi và nhận SMS
        checkAndRequestPermissions();
    }

    // Phương thức này luôn chạy trên luồng UI vì nó được gọi từ LiveData observer.
    private void processNewAddresses(ArrayList<String> addresses) {
        boolean listChanged = false;
        reentrantLock.lock();
        try {
            for (String address : addresses) {
                if (!requesters.contains(address)) {
                    requesters.add(address);
                    listChanged = true;
                }
            }
        } finally {
            reentrantLock.unlock();
        }

        if (listChanged) {
            adapter.notifyDataSetChanged();
        }

        if (swAutoResponse.isChecked()) {
            respond(true);
        }
    }


    private void respond(String to, String response) {
        reentrantLock.lock();
        try {
            requesters.remove(to);
            adapter.notifyDataSetChanged();
        } finally {
            reentrantLock.unlock();
        }

        SmsManager smsManager = SmsManager.getDefault();
        // Cần quyền SEND_SMS để dòng này hoạt động
        smsManager.sendTextMessage(to, null, response, null, null);
    }

    private void respond(boolean ok) {
        String okString = getString(R.string.i_am_safe_and_well_worry_not);
        String notOkString = getString(R.string.tell_my_mother_i_love_her);
        String outputString = ok ? okString : notOkString;

        ArrayList<String> requestersCopy;
        reentrantLock.lock();
        try {
            requestersCopy = new ArrayList<>(requesters);
        } finally {
            reentrantLock.unlock();
        }

        for (String to : requestersCopy) {
            respond(to, outputString);
        }
    }

    private void handleOnClickListenner() {
        btnSafe.setOnClickListener(v -> respond(true));
        btnMayday.setOnClickListener(v -> respond(false));

        swAutoResponse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            llButtons.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            editor.putBoolean(AUTO_RESPONSE, isChecked);
            editor.apply();
        });
    }

    private void initVariables() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        reentrantLock = new ReentrantLock();
        requesters = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requesters);
        lvMessages.setAdapter(adapter);

        boolean autoResponse = sharedPreferences.getBoolean(AUTO_RESPONSE, false);
        swAutoResponse.setChecked(autoResponse);
        llButtons.setVisibility(autoResponse ? View.GONE : View.VISIBLE);
    }

    private static final int SMS_PERMISSIONS_REQUEST_CODE = 102;
    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS
        };
        // Kiểm tra xem các quyền đã được cấp chưa
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa, yêu cầu người dùng cấp quyền
            requestPermissions(permissions, SMS_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permissions denied. The app may not work correctly.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }
}
