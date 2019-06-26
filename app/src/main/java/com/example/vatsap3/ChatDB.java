package com.example.vatsap3;

public class ChatDB {

    private String message;

    public ChatDB(){

    }

    public ChatDB(String message){
        this.message=message;
    }
    public String getMessage(){
        if(message != null) {
            return message;
        } else if(message == null) {
            return "";
        } else {
            return "";
        }
    }
}
