package com.example.lab2_2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView lvPerson;
    TextView tvPerson;
    ArrayList<String> arrayName;
    Button btnAdd;
    EditText edtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPerson = findViewById(R.id.textView);
        lvPerson = findViewById(R.id.listView);
        edtName = findViewById(R.id.editName);

        btnAdd = findViewById(R.id.button4);
        arrayName = new ArrayList<>();
        arrayName.add("Tèo");
        arrayName.add("Tý");
        arrayName.add("Bin");
        arrayName.add("Bo");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayName);
        lvPerson.setAdapter(adapter);
        btnAdd.setOnClickListener(view -> {
            String ten = edtName.getText().toString().trim(); // .trim() để loại bỏ khoảng trắng thừa
            if (!ten.isEmpty()) { // Chỉ thêm nếu chuỗi không rỗng
                arrayName.add(ten);
                adapter.notifyDataSetChanged();
                edtName.setText(""); // Xóa nội dung trong EditText sau khi thêm thành công
            }
        });


        lvPerson.setOnItemClickListener((parent, view, position, id) -> {
            String value = adapter.getItem(position);
            tvPerson.setText("position :" + position + "; value =" + value);
        });

        lvPerson.setOnItemLongClickListener((parent, view, position, id) -> {
            arrayName.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });

    }

}