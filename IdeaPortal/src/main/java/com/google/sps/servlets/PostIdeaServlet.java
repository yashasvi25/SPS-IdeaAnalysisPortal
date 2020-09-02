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
import com.google.sps.servlets.LoginServlet;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.sps.data.ProductIdea;
import java.util.Map;


@WebServlet("/product-idea")
public class PostIdeaServlet extends HttpServlet {

  private static DatastoreService datastore;
  private static BlobstoreService blobstoreService;
  private static ImagesService imagesService;
  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    imagesService = ImagesServiceFactory.getImagesService();
  }

  /*post function to read input of the form and add the comment to the database*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Entity user = LoginServlet.getUser(request);
    long userID = user.getKey().getId();
    // Get the input from the form.
    String title = request.getParameter("title");
    String description = request.getParameter("description");
    String category = request.getParameter("category");
    long timestamp = System.currentTimeMillis();
    String imageUrl = getUploadedFileUrl(request, "image");
    if(imageUrl!=null && (imageUrl.substring(0,4)).equals("http")){
        imageUrl = "https"+imageUrl.substring(4);
    }
    // System.out.println(request.getParameter("category").getClass().getName());

    Entity taskEntity = new Entity("ProductIdea");
    taskEntity.setProperty("authorid",userID);
    taskEntity.setProperty("title", title);
    taskEntity.setProperty("description", description);
    taskEntity.setProperty("category", category);
    taskEntity.setProperty("timestamp", timestamp);
    taskEntity.setProperty("imageUrl",imageUrl);

    datastore.put(taskEntity);

    response.sendRedirect("/dashboard.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity user = LoginServlet.getUser(request);
    long userID = user.getKey().getId();

    Filter propertyFilter = new FilterPredicate("authorid", FilterOperator.EQUAL, userID);
    Query query = new Query("ProductIdea").setFilter(propertyFilter);

    PreparedQuery results = datastore.prepare(query);
    List<ProductIdea> productIdeas = getProductIdeasList(results);
    String json = convertToJson(productIdeas);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private List<ProductIdea> getProductIdeasList(PreparedQuery results){

    List<ProductIdea> productIdeas = new ArrayList<>();
    for (Entity entity : results.asIterable()) {

      long productID = (long) entity.getKey().getId();
      String title = (String) entity.getProperty("title");
      long authorID = (long) entity.getProperty("authorid");
      long timestamp = (long) entity.getProperty("timestamp");
      String category = (String) entity.getProperty("category");
      String imageUrl = (String) entity.getProperty("imageUrl");
      String description = (String) entity.getProperty("description");
      
      ProductIdea finalProductIdea = new ProductIdea(productID, title, authorID, timestamp, category, imageUrl, description);
      productIdeas.add(finalProductIdea);
    }
    return productIdeas;
  }

  /*Converts a list of product ideas instance into a JSON string using the Gson library.*/
  private String convertToJson(List< ProductIdea > productIdeas) {
    Gson gson = new Gson();
    String json = gson.toJson(productIdeas);
    return json;
  }

/** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(formInputElementName);

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
        return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
        blobstoreService.delete(blobKey);
        return null;
    }

    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    String url = imagesService.getServingUrl(options);

    // GCS's localhost preview is not actually on localhost,
    // so make the URL relative to the current domain.
    if(url.startsWith("http://localhost:8080/")){
        url = url.replace("http://localhost:8080/", "/");
    }
    return url;
  }

}
