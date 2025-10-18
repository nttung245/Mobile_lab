package com.example.lab02_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.lab02_3.R;
import com.example.lab02_3.employee_type.Employee;
import com.example.lab02_3.employee_type.EmployeeFulltime;
import com.example.lab02_3.employee_type.EmployeeParttime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvPerson;
    private TextView tvSelection;
    private Button btnSubmit;
    private EditText etName;
    private EditText etId;
    private RadioGroup rgType;
    private ArrayList<Employee> employees;
    private RadioButton rd_chinhthuc;

    private void initUi()
    {
        lvPerson = findViewById(R.id.lv_person);
        tvSelection = findViewById(R.id.tv_selection);
        btnSubmit = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.etEnterTenNV);
        etId = findViewById(R.id.etEnterMaNV);
        rgType = findViewById(R.id.rgType);
        rd_chinhthuc = findViewById(R.id.rd_chinhthuc);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        employees = new ArrayList<Employee>();
        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this, android.R.layout.simple_list_item_1, employees);
        lvPerson.setAdapter(adapter);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Thêm dữ liệu mới vào arraylist
                addNewEmployee();
                //Cập nhật dữ liệu mới lên giao diên
                adapter.notifyDataSetChanged();
            }
        });

        //5. Xử lý sự kiện chọn một phần tử trong ListView
        lvPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?>arg0, View arg1, int arg2, long arg3) {
                //đối số arg2 là vị trí phần tử trong Data Source (arr)
                tvSelection.setText("position :" + arg2 + "; value =" + employees.get(arg2).toString());
            }
        });
    }

    public void addNewEmployee() {
        //Lấy ra đúng id của Radio Button được checked
        Employee employee = new Employee();
        int radId = rgType.getCheckedRadioButtonId();
        String id = etId.getText().toString();
        String name = etName.getText().toString();
        if (radId == R.id.rd_chinhthuc) {
            //tạo instance là FullTime
            employee = new EmployeeFulltime();
        } else {
            //Tạo instance là Partime
            employee = new EmployeeParttime();
        }
        //FullTime hay Partime thì cũng là Employee nên có các hàm này là hiển nhiên
        employee.setId(id);
        employee.setName(name);
        //Đưa employee vào ArrayList
        employees.add(employee);

        etId.setText("");
        etName.setText("");
        rgType.clearCheck();

    }

}