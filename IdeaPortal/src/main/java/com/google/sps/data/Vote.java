package com.google.sps.data;

public final class Vote{
    private final long productID;
    private final long authorId;
    private Integer voteValue; // -1-> downvote , 0 -> no vote, 1 -> upvote

    public Vote(long productID,long authorId, Integer voteValue){
        this.productID= productID;
        this.authorId = authorId;
        this.voteValue = voteValue;
    }

    //Will add getters and setters as per requirement
}
