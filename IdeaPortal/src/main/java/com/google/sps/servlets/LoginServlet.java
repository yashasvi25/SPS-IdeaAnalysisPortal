// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  private static DatastoreService datastore;
  private static UserService userService;
  private static String email;

  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    userService = UserServiceFactory.getUserService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { 

    String thisUrl = request.getRequestURI();

    response.setContentType("text/html");
    if (request.getUserPrincipal() != null) {
        email = request.getUserPrincipal().getName();
        Filter propertyFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
        Query query = new Query("User").setFilter(propertyFilter);
        List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        if(results.size() == 0){
            Entity taskEntity = new Entity("User");
            taskEntity.setProperty("email", email);
            datastore.put(taskEntity);
        }
        response.sendRedirect("/dashboard.html");
                
    } else {
        response.sendRedirect(userService.createLoginURL(thisUrl));
    }
  }

  public static Entity getUser(HttpServletRequest request){
    Filter propertyFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
    if(request.getUserPrincipal() != null)
        propertyFilter = new FilterPredicate("email", FilterOperator.EQUAL, request.getUserPrincipal().getName());
    Query query = new Query("User").setFilter(propertyFilter);
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
    Entity user = results.get(0);
    return user;
  }

}
