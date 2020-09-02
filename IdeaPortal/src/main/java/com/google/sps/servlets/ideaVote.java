package com.google.sps.servlets;
import com.google.sps.servlets.ideaComments;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.util.Arrays;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ideaVote")
public class ideaVote extends HttpServlet { 

  private static DatastoreService datastore;
  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /*post function to read input of the form and add the comment to the database*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Entity user = LoginServlet.getUser(request);

    long authorId = user.getKey().getId();

    Filter productFilter = new FilterPredicate("productID", FilterOperator.EQUAL, ideaComments.productID);
    Filter authorFilter = new FilterPredicate("authorId", FilterOperator.EQUAL, authorId);

    CompositeFilter filter = CompositeFilterOperator.and(productFilter, authorFilter);
    Query query = new Query("Vote").setFilter(filter);
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
    Entity taskEntity = new Entity("Vote");
    if(results.size()>0){
        taskEntity = results.get(0);
    }
    Integer voteValue = Integer.parseInt(request.getParameter("voteValue"));
    double sentimentAnalysisScore = 9.5;

    taskEntity.setProperty("productID",ideaComments.productID);
    taskEntity.setProperty("authorId",authorId);
    taskEntity.setProperty("voteValue",voteValue);

    datastore.put(taskEntity);

    response.sendRedirect("/IdeaPage.html");
  }

}



