package com.example.vatsap3;

public class User {

    private String adSoyad;
    private String eMail;
    private String password;

    public User() {

    }

    public User(String adSoyad, String eMail, String password){
        this.adSoyad=adSoyad;
        this.eMail=eMail;
        this.password=password;
    }

    public String getAdSoyad(){
        return adSoyad;
    }

    public String geteMail(){
        return eMail;
    }

    public String getpassword(){
        return password;
    }
}
