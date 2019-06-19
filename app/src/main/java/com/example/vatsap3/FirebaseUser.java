package com.example.vatsap3;

public class FirebaseUser { //Firebase için gerekli Class

    private String adSoyad;
    private String eMail;
    private String password;
    //private int id;

    public FirebaseUser() {

    }

    public FirebaseUser(String adSoyad, String eMail, String password){
        this.adSoyad=adSoyad;
        this.eMail=eMail;
        this.password=password;
        //this.id=id;
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

//    public int getId(){
//        return id;
//    }

}
