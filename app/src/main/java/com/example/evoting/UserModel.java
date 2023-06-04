package com.example.evoting;

public class UserModel {
    String email;
    String password;
    int age;
    long cnic;

    public UserModel(String email, String password, int age, long cnic) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.cnic = cnic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getCnic() {
        return cnic;
    }

    public void setCnic(long cnic) {
        this.cnic = cnic;
    }
}
