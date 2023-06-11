package com.example.todoapp.utils;

public class Task {
    public int Id;

    public String Title;

    public String Description;

    public byte[] Image;

    public int getId(){
        return Id;
    }

    public String getTitle(){
        return Title;
    }

    public String getDescription(){
        return Description;
    }

    public byte[] getImage(){
        return Image;
    }


    public void setTitle(String title){
        this.Title = title;
    }

    public void setDescription(String description){
        this.Description = description;
    }

    public void setId(int id){this.Id = id;}

    public void setImage(byte[] image){this.Image = image;}

}
