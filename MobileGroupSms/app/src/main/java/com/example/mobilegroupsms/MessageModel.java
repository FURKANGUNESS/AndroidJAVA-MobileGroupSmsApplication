package com.example.mobilegroupsms;

import java.util.List;

//Recycle Adapter icin olusturdugum messagemodel classim.
public class MessageModel {
    private String name, description, uid;

    public MessageModel(String name, String description, String uid) {
        this.name = name;
        this.description = description;
        this.uid = uid;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getUid(){
        return uid;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setUid(String uid){
        this.uid=uid;
    }
}
