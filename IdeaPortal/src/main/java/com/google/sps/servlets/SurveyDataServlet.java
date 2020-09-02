package com.google.sps.servlets;



import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Survey;
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

class ProjectAgeCount{
    private final long productID;
    private ArrayList<Integer> ageGroupCount;
    
    ProjectAgeCount(final long productID){
        this.productID= productID;
        this.ageGroupCount = new ArrayList<Integer>();
        ageGroupCount = new ArrayList<Integer>();
        for(int i=0;i<4;i++)
            ageGroupCount.add(0);
    }

    void incrementAgeCount(int index ){
        if(index==-1)
            return;
        Integer value = this.ageGroupCount.get(index); // get value
        value = value + 1; // increment value
        this.ageGroupCount.set(index, value); // replace value
       
    }
   
}

/** Servlet responsible for listing tasks. */
@WebServlet("/age-count")
public class SurveyDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    long productID= Long.parseLong(request.getParameter("productid"));
    ProjectAgeCount obj= getProjectAgeCountObject(productID) ;

    
    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(obj));
  }


  ProjectAgeCount getProjectAgeCountObject(final long productID){
      //iterate over results and create ProjectVote object
      ProjectAgeCount ageCountObj= new ProjectAgeCount(productID);

      PreparedQuery results= getQueryResults(productID);

      for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        
        Integer posValue= ((Long)entity.getProperty("ageGroupCount")).intValue();;
        ageCountObj.incrementAgeCount(posValue);
        }

      return ageCountObj;

  }

  PreparedQuery getQueryResults(final long productID){
      //build and prepare query results
        Filter projectIDFilter =
             new FilterPredicate("productID", FilterOperator.EQUAL, productID);
        Query query = new Query("Survey").setFilter(projectIDFilter);
   

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    return results;  
  }

}
