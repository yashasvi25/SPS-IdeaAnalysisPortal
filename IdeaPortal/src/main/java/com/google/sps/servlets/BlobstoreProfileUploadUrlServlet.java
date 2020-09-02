package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When the fetch() function requests the /blobstore-profile-upload-url URL, the content of the response is
 * the URL that allows a user to upload a file to Blobstore.
 */
@WebServlet("/blobstore-profile-upload-url")
public class BlobstoreProfileUploadUrlServlet extends HttpServlet {

  private static BlobstoreService blobstoreService;

  @Override
  public void init(){
    blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String uploadUrl = blobstoreService.createUploadUrl("/profile-info");

    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);
  }
} 