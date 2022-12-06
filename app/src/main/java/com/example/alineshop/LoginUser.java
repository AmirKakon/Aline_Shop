package com.example.alineshop;

public class LoginUser {
    private String m_username;
    private String m_password;
    private final String EMPTY_SPOT = " ";

    public LoginUser(){
        m_username = " ";
        m_password = " ";
    }

    public LoginUser(String username){
        if(username.isEmpty())
            this.m_username = EMPTY_SPOT;
        else
            this.m_username = username;
        this.m_password = EMPTY_SPOT;

    }

    public LoginUser(String username, String password) {
        if(username.isEmpty())
            this.m_username = EMPTY_SPOT;
        else
            this.m_username = username;

        if(password.isEmpty())
            m_password = EMPTY_SPOT;
        else
            this.m_password = password;
    }

    public String getUsername() {
        return m_username;
    }

    public void setUsername(String username) {
        this.m_username = username;
    }

    public String getPassword() {
        return m_password;
    }

    public void setPassword(String password) {
        this.m_password = password;
    }

    public void resetUser() {
        this.m_username = EMPTY_SPOT;
        this.m_password = EMPTY_SPOT;
    }

    public void setUser(String email, String password){
        if(!email.isEmpty())
            this.m_username = email;
        else
            this.m_username = EMPTY_SPOT;

        if(!password.isEmpty())
            this.m_password = password;
        else
            this.m_password = EMPTY_SPOT;
    }

    public boolean isEmptyUsername(){
        if(this.m_username == EMPTY_SPOT || this.m_username.isEmpty())
            return true;
        return false;
    }

    public boolean isEmptyPassword(){
        if(this.m_password == EMPTY_SPOT || this.m_password.isEmpty())
            return true;
        return false;
    }

    public boolean isEmptyUser(){
        if (isEmptyUsername() || isEmptyPassword())
            return true;
        return false;
    }
}
