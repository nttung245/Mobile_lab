package com.example.lab3_1;
import java.util.Random;
public class Contact {
    private int id;
    private String name;
    private String phoneNumber;

    // Constructor
    public Contact() {
        this.id = 0;
        this.name = "";
        this.phoneNumber = "";
    }

    // Constructor with parameters

    public Contact(String name, String phoneNumber) {
        this.id = new Random().nextInt(1000000);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
