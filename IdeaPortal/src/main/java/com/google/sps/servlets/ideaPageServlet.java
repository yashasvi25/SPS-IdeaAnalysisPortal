package com.google.sps.servlets;
import com.google.sps.data.ProductIdea;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ideaPage")
public class ideaPageServlet extends HttpServlet { 

  private static DatastoreService datastore;
  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    long ProductID= Long.parseLong(request.getParameter("productid"));

    Filter ProductIDFilter = new FilterPredicate("__key__", FilterOperator.EQUAL, KeyFactory.createKey("ProductIdea",ProductID));
    Query query = new Query("ProductIdea").setFilter(ProductIDFilter);
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
    Entity entity = results.get(0);

    long productID = (long) entity.getKey().getId();
    String title = (String) entity.getProperty("title");
    long authorID = (long) entity.getProperty("authorid");
    long timestamp = (long) entity.getProperty("timestamp");
    String category = (String) entity.getProperty("category");
    String imageUrl = (String) entity.getProperty("imageUrl");
    String description = (String) entity.getProperty("description");
    
    ProductIdea finalProductIdea = new ProductIdea(productID, title, authorID, timestamp, category, imageUrl, description);
    
    Gson gson = new Gson();
    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(finalProductIdea));
  } 

}



