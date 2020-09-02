package com.google.sps.servlets;

import com.google.gson.Gson;
import java.util.ArrayList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity; 
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.ProductIdea;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet("/CommonFeed/*")
public class onFeedLoad extends HttpServlet {

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String queryString = request.getQueryString();
    String CategorySelected = queryString.substring(9);
    Query query;

    if(CategorySelected.equals("All"))
       {
            query  = new Query("ProductIdea").addSort("timestamp", SortDirection.DESCENDING); 
       }
    else
    {
        Filter categoryFilter = new FilterPredicate("category", FilterOperator.EQUAL, CategorySelected);
        query = new Query("ProductIdea").setFilter(categoryFilter);
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

  
    ArrayList<ProductIdea> ideas = new ArrayList<ProductIdea>();

    for (Entity entity : results.asIterable()) {   

    long productID = (long) entity.getKey().getId();  
    String title = (String) entity.getProperty("title");
    long authorID = (long) entity.getProperty("authorid") ;
    long timestamp = (long) entity.getProperty("timestamp");
    String category = (String) entity.getProperty("category");
    String imageUrl = (String) entity.getProperty("imageUrl");
    String description = (String) entity.getProperty("description");

    ProductIdea idea = new ProductIdea(productID, title, authorID, timestamp, category, imageUrl, description);
        ideas.add(idea);
      }

    //Convert list to JSON 
    String json = convertToJson(ideas);

    response.setContentType("application/json");
    response.getWriter().println(json);
    
  }

  private String convertToJson(ArrayList<ProductIdea> ideas)
  {
    Gson gson = new Gson();
    String json = gson.toJson(ideas);

    return json;
  }
}