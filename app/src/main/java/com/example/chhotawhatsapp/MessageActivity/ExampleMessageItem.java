package com.example.chhotawhatsapp.MessageActivity;

public class ExampleMessageItem {

    private String message;
    private String time;
    private String sender;

    public ExampleMessageItem(String message, String time,String sender) {
        this.message = message;
        this.time = time;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }
}
