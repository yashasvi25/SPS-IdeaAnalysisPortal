package com.google.sps.data;
import java.util.Arrays; 
import java.util.ArrayList; 

public final class Survey{
    private final long productID;
    private final long authorId;
    private final Integer ageGroupCount; //0-14, 15-24, 25-65, >65 //Represents index of age group from 0 to 3

    public Survey(long productID, long authorId, Integer ageGroupCount){
        this.productID= productID;
        this.authorId = authorId;
        this.ageGroupCount = ageGroupCount;
    }

    //Will add getters and setters as per requirement
}
