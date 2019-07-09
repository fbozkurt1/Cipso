package com.example.cipso;

public class Message {
    private String textMessage;
    private String date;
    //private Contacts contacts;
    private String uid;


    public Message(String textMessage,String dateMessage ,String uid) {
        this.textMessage = textMessage;
        this.date = dateMessage;
        this.uid=uid;
    }

    public Message() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

}
