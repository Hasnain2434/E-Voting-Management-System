package com.example.evoting;

public class UserModel {
    String email;
    String password;
    int age;
    String cnic;

    String vote;
    String casted;

    public String getCasted() {
        return casted;
    }

    public void setCasted(String casted) {
        this.casted = casted;
    }

    public UserModel(String email, String password, int age, String cnic, String vote) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.cnic = cnic;
        this.vote = vote;
        casted="false";
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
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

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}
