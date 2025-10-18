package com.example.lab02_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtName;
    CheckBox chbxManager;
    Button btnAdd;
    ListView lv_Employee;
    ArrayList<Employee> employees;
    EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);

        edtName = findViewById(R.id.edtName);
        chbxManager = findViewById(R.id.chbxManager);
        btnAdd = findViewById(R.id.btnAdd);
        lv_Employee = findViewById(R.id.lv_Employee);
        employees = new ArrayList<Employee>();

        adapter = new EmployeeAdapter(this, R.layout.item_employee,employees);
        lv_Employee.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                // Do not add an employee if the name is empty
                if (name.trim().isEmpty()) {
                    edtName.setError("Name cannot be empty");
                    return;
                }

                Employee employee = new Employee();
                employee.setManager(chbxManager.isChecked());
                employee.setFullName(name);

                employees.add(employee);
                adapter.notifyDataSetChanged();

                // Clear input for next entry
                edtName.setText("");
                chbxManager.setChecked(false);
                edtName.requestFocus();
            }
        });

    }
}