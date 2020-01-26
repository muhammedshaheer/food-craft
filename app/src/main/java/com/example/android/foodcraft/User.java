package com.example.android.foodcraft;

/**
 * Created by User on 10/28/2018.
 */

public class User {
    public String name,email,phone;

    public  User(){

    }

    public User(String name ,String email, String phone) {
        this.name = name;
        this.email=email;
        this.phone = phone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
