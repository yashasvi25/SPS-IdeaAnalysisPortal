package com.google.sps.data;

import java.util.ArrayList; 
import java.util.Arrays; 

public final class ProductIdea{

    enum categories {
        Sustainability,
        Privacy,
        Agriculture,
        Tourism,
        Other,  
    }
    private final long productID;
    private final String title;
    private final long authorID;
    private final long timestamp;
    private final String category;
    private final String imageUrl;
    private final String description;

    //To be calculated during loading of stats page
    // private double finalRating;
    // private Arrays<int> sentimentScoreCount; //0-20, 20-40, 40-60, 60-80, 80-100
    // private int upvotes;
    // private int downvotes;


    public ProductIdea(long productID, String title, long authorID, long timestamp, String category, String imageUrl, String description){
        this.productID = productID;
        this.title = title;
        this.authorID = authorID;
        this.timestamp = timestamp;
        this.category = category;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    //Will add getters and setters as per requirement

}