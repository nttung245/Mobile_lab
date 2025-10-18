package com.example.lab2_1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ListView lvPerson;
    private TextView tvSelection;

    private final String[] arr = {"Teo", "Ty", "Bin", "Bo"};

    private void initUi()
    {
        lvPerson = findViewById(R.id.listView);
        tvSelection = findViewById(R.id.textView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bật chế độ EdgeToEdge
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // Đoạn code này xử lý padding để nội dung không bị che khuất
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_list_item, arr);
        lvPerson.setAdapter(adapter);

        lvPerson.setOnItemClickListener((parent, view, position, id) -> {
            // Tham số arg2 được đổi tên thành position để dễ hiểu hơn
            tvSelection.setText("position :" + position + "; value =" + arr[position]);
        });
    }

}