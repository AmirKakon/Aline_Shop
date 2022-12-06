package com.example.alineshop;

public class UserModel {
    private String m_name, m_email, m_phoneNumber;

    public UserModel() {

    }

    public UserModel(String m_name, String m_email, String m_phoneNumber) {
        this.m_name = m_name;
        this.m_email = m_email;
        this.m_phoneNumber = m_phoneNumber;
    }

//    public UserModel(UserModel userModel){
//        this.m_name = userModel.m_name;
//        this.m_email = userModel.m_email;
//        this.m_phoneNumber = userModel.m_phoneNumber;
//    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        this.m_name = name;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        this.m_email = email;
    }

    public String getPhoneNumber() {
        return m_phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.m_phoneNumber = phoneNumber;
    }
}
