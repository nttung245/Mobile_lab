package com.example.lab02_3.employee_type;

public class EmployeeParttime extends Employee{
    @Override
    public double tinhLuong() {
        return 150;
    }

    @Override
    public String toString() {
        return super.toString() + "--> Partime = " + tinhLuong();
    }

}
