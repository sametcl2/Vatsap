package com.example.vatsap3;

public class ChatDB {

    String lastMessage;

    public ChatDB(){

    }

    public ChatDB(String lastMessage){
        this.lastMessage=lastMessage;
    }

    public String getMessage(){
        return lastMessage;
    }

}
