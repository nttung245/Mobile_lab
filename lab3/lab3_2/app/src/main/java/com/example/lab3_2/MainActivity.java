package com.example.lab3_2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editAVGScore;
    private StudentAdapter studentAdapter;
    private SQLiteDatabase database;
    private DatabaseHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editAVGScore = findViewById(R.id.editAVGScore);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        RecyclerView recyclerViewStudents = findViewById(R.id.recyclerViewStudents);

        // Setup RecyclerView
        studentAdapter = new StudentAdapter();
        studentAdapter.setOnItemClickListener(this);
        recyclerViewStudents.setAdapter(studentAdapter);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        // Setup Database
        dbHelper = new DatabaseHandler(this);
        database = dbHelper.getWritableDatabase();

        // Load initial data from the database
        loadStudentsFromDatabase();

        // Set listener for the "Add" button
        buttonAdd.setOnClickListener(v -> addStudent());
    }

    private void loadStudentsFromDatabase() {
        // Use try-with-resources to ensure the cursor is always closed
        try (Cursor cursor = database.rawQuery("SELECT * FROM students", null)) {
            // Check if there is data
            if (cursor.moveToFirst()) {
                do {
                    // It's safer to use getColumnIndexOrThrow for debugging column name issues
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
                    @SuppressLint("Range") float score = cursor.getFloat(cursor.getColumnIndex("score"));

                    Student student = new Student(id, name, age, score);
                    studentAdapter.addStudent(student);
                } while (cursor.moveToNext());
            }
        }
    }

    private void addStudent() {
        String name = editTextName.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();
        String scoreStr = editAVGScore.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(scoreStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            float score = Float.parseFloat(scoreStr);

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);
            values.put("score", score);

            long id = database.insert("students", null, values);

            Student student = new Student((int) id, name, age, score);
            studentAdapter.addStudent(student);

            // Clear input fields after successful addition
            editTextName.setText("");
            editTextAge.setText("");
            editAVGScore.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format for age or score", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(final Student student) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Student");
        View view = LayoutInflater.from(this).inflate(R.layout.edit_student, null);

        // These IDs must match your edit_student.xml layout file
        final EditText editDialogName = view.findViewById(R.id.edit1TextName);
        final EditText editDialogAge = view.findViewById(R.id.edit1TextAge);
        final EditText editDialogAVGScore = view.findViewById(R.id.edit1TextAVGScore);

        // Populate the dialog with existing student data
        editDialogName.setText(student.getName());
        editDialogAge.setText(String.valueOf(student.getAge()));
        editDialogAVGScore.setText(String.valueOf(student.getScore()));

        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = editDialogName.getText().toString().trim();
            String ageStr = editDialogAge.getText().toString().trim();
            String scoreStr = editDialogAVGScore.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(scoreStr)) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                float score = Float.parseFloat(scoreStr);

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("age", age);
                values.put("score", score);

                database.update("students", values, "id = ?", new String[]{String.valueOf(student.getId())});
                studentAdapter.updateStudent(new Student(student.getId(), name, age, score));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            database.delete("students", "id = ?", new String[]{String.valueOf(student.getId())});
            studentAdapter.deleteStudent(student);
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Always close database connections to prevent memory leaks
        if (database != null) {
            database.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

