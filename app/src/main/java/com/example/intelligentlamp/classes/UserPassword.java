package com.example.intelligentlamp.classes;

import org.litepal.crud.DataSupport;

/**
 * Created by 76377 on 2017/12/17.
 */

public class UserPassword extends DataSupport{
    private String user;

    private String password;

    private byte[] headShot;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public byte[] getHeadShot() {return headShot;}

    public void setHeadShot(byte[] headShot) {this.headShot = headShot;}
}

