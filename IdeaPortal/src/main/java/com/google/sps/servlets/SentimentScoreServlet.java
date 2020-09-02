package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

/** Servlet responsible for listing tasks. */
@WebServlet("/sentiment-score")
public class SentimentScoreServlet extends HttpServlet {

    private static DatastoreService datastore;

    public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    long productID= Long.parseLong(request.getParameter("productid"));
    List<Comment> comments= getListofCommentObject(productID);

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

private List<Comment> getListofCommentObject(final long productID){
    
  
    PreparedQuery results= getQueryResults(productID);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        long commentAuthorId= (long) entity.getProperty("commentAuthorId");
        String suggestion = (String) entity.getProperty("suggestion");
        long timestamp = (long) entity.getProperty("timestamp");
        String text = (String) entity.getProperty("text");
        double sentimentAnalysisScore = (double) entity.getProperty("sentimentAnalysisScore");

        Comment comment_obj = new Comment(productID,commentAuthorId,text,suggestion,timestamp,sentimentAnalysisScore);
        comments.add(comment_obj);
    }
    return comments;
}

private PreparedQuery getQueryResults(final long productID){


      //build and prepare query results
        Filter projectIDFilter =
             new FilterPredicate("productID", FilterOperator.EQUAL, productID);
        Query query = new Query("Comment").setFilter(projectIDFilter);
   

        //DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
       
        PreparedQuery results = datastore.prepare(query);
        return results;  
  }

  
}



