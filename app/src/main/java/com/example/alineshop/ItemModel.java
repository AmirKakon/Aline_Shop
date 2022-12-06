package com.example.alineshop;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ItemModel implements Serializable {
    private String m_name, m_color, m_size, m_owner;
    //private Timestamp m_timestamp;

    public ItemModel() {
    }

    public ItemModel(String name, String owner){
        this.m_name = name;
        this.m_color = "";
        this.m_size = "";
        this.m_owner = owner;
        //this.m_timestamp = Timestamp.now();
    }
    public ItemModel(String color, String name,String owner, String size){
        this.m_color = color;
        this.m_name = name;
        this.m_owner = owner;
        this.m_size = size;
        //this.m_timestamp = Timestamp.now();
    }

//    public ItemModel(String name, String color, String size, String owner, Timestamp timestamp){
//        this.m_name = name;
//        this.m_color = color;
//        this.m_size = size;
//        this.m_owner = owner;
//        this.m_timestamp = timestamp;
//    }

//    public ItemModel(ItemModel item){
//        this.m_name = item.m_name;
//        this.m_color = item.m_color;
//        this.m_size = item.m_size;
//        this.m_owner = item.m_owner;
//        //this.m_timestamp = item.m_timestamp;
//    }

    public String getSize() {
        return m_size;
    }

    public void setSize(String size) {
        this.m_size = size;
    }

    public String getColor() {
        return m_color;
    }

    public void setColor(String color) {
        this.m_color = color;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        this.m_name = name;
    }

    public String getOwner() { return m_owner;}

    private void setOwner(String owner) { this.m_owner = owner;}

//    public Timestamp getTimestamp() { return m_timestamp;}
//
//    private void setTimestamp(Timestamp timestamp) { this.m_timestamp = timestamp;}
}
