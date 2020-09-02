package com.google.sps.data;
import java.util.ArrayList; 
import java.util.List;
import java.util.Arrays;

public final class Comment{
    private final long productID;
    private final long commentAuthorId;
    private final String text;
    private final String suggestion;
    private ArrayList<String> keyWords;
    private final long timestamp;
    private double sentimentAnalysisScore;

    public Comment(long productID, long commentAuthorId, String text, String suggestion, long timestamp, double sentimentAnalysisScore){
        this.productID = productID;
        this.commentAuthorId = commentAuthorId;
        this.text = text;
        this.suggestion = suggestion;
        this.timestamp = timestamp;
        this.sentimentAnalysisScore = sentimentAnalysisScore;
    }

    public void setKeywords(ArrayList<String> keyWords){
        this.keyWords = keyWords;
    }

    //Will add getters and setters as per requirement
}
