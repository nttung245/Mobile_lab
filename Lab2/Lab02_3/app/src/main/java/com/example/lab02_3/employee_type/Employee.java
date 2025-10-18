package com.example.lab02_3.employee_type;

public class Employee {
    private String id;
    private String name;

    private boolean is_manager;

    public Employee(){

    }

    public Employee(String id, String name, boolean is_manager){
        this.id = id;
        this.name = name;
        this.is_manager = is_manager;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getIsManager(){
        return this.is_manager;
    }

    public void setIsManager(boolean is_manager){
        this.is_manager = is_manager;
    }

    public double tinhLuong(){
        return 0;
    }

    @Override
    public String toString() {
        return this.id + " - " + this.name;
    }
}
