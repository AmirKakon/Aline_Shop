package com.example.alineshop;

import java.io.Serializable;

public class UserModelTest implements Serializable {

    private String userName;

    public UserModelTest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
}
