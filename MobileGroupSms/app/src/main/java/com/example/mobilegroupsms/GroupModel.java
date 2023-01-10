package com.example.mobilegroupsms;

import java.util.List;

//Dinamik verilerimi depolayabilecegim java classim.
public class GroupModel {
    private String name, description, image, uid;
    private List<String> numbers;  //List olarak numaralari da tutmak istiyorum.

    public GroupModel() {
    }

    public GroupModel(String name, String description, String image, String uid,List<String>numbers) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.uid = uid;
        this.numbers=numbers;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image=image;
    }
    public String getUid(){
        return name;
    }
    public void setUid(String uid){
        this.uid=uid;
    }
    public List<String> getNumbers(){
        return numbers;
    }
    public void setNumbers(List<String> numbers){
        this.numbers=numbers;
    }
}
