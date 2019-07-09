package com.example.cipso;

public class Contacts {
    private String name;
    private String surname;
    private String phoneNum;
    private String email;
    private String uId;

    public Contacts(String name, String surname, String phoneNum, String email) {
        this.name = name;
        this.surname = surname;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public Contacts() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


}
