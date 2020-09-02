package com.google.sps.data;
import java.util.Date;

public final class User{
    private final String email;
    private String name;
    private Date dob;
    private String teamName;
    private String imageUrl;

    public User(String email, String name, Date dob, String teamName, String imageUrl){
        this.email = email;
        this.name = name;
        this.dob = dob;
        this.teamName = teamName;
        this.imageUrl = imageUrl;
    }

    //Will add getters and setters as per requirement

}