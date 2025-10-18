package com.example.lab02_3.employee_type;

public class EmployeeFulltime extends Employee{
    @Override
    public double tinhLuong() {
        return 500;
    }

    @Override
    public String toString() {
        return super.toString() + " --> FullTime = " + tinhLuong();
    }
}
