package com.google.sps.servlets;



import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Vote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

class ProjectVote{
    private final long productID;
    private long upvotes;
    private long downvotes;

    ProjectVote(final long productID){
        this.productID= productID;
        this.upvotes=0;
        this.downvotes=0;
    }

    void incrementUpvote(){
        this.upvotes+= 1;
    }
    void incrementDownvote(){
        this.downvotes+=1;
    }

    long getUpvotes(){
        return this.upvotes;
    }
    long getDownvotes(){
        return this.downvotes;
    }
}

/** Servlet responsible for listing tasks. */
@WebServlet("/vote-response")
public class VoteResponseServlet extends HttpServlet {
    private static DatastoreService datastore;

    public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    long productID= Long.parseLong(request.getParameter("productid"));
    ProjectVote obj= getProjectVoteObject(productID) ;
    
    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(obj));
  }
  ProjectVote getProjectVoteObject(final long productID){
      //iterate over results and create ProjectVote object
      ProjectVote voteObj= new ProjectVote(productID);

      PreparedQuery results= getQueryResults(productID);

      for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        
        long voteValue= (long) entity.getProperty("voteValue");
       
        if(voteValue==1)
            voteObj.incrementUpvote();
       
        if(voteValue==-1)
            voteObj.incrementDownvote();

         }

      return voteObj;

  }

  PreparedQuery getQueryResults(final long productID){
      //build and prepare query results
        Filter productIDFilter =
             new FilterPredicate("productID", FilterOperator.EQUAL, productID);
        Query query = new Query("Vote").setFilter(productIDFilter);
   

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    return results;  
  }

}
