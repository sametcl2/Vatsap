package com.example.vatsap3;

import android.graphics.Bitmap;

import com.github.bassaer.chatmessageview.model.IChatUser;

public class User implements IChatUser {

    String myId;
    String isim;

    public User(String myId, String isim){
        this.myId=myId;
        this.isim=isim;
    }


    @Override
    public Bitmap getIcon() {
        return null;
    }

    @Override
    public String getId() {
        return myId;
    }

    @Override
    public String getName() {
        return isim;
    }

    @Override
    public void setIcon(Bitmap bitmap) {

    }
}
