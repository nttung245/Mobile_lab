package com.example.lab3_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentView> {
    private final List<Student> students = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public StudentView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentView holder, int position) {
        Student student = students.get(position);
        holder.bind(student, listener);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void addStudent(Student student) {
        students.add(student);
        notifyItemInserted(students.size() - 1);
    }

    public void updateStudent(Student student) {
        int index = students.indexOf(student);
        if (index != -1) {
            students.set(index, student);
            notifyItemChanged(index);
        }
    }

    public void deleteStudent(Student student) {
        int index = students.indexOf(student);
        if (index != -1) {
            students.remove(index);
            notifyItemRemoved(index);
        }
    }
}