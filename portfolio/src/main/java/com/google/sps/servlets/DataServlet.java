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

import java.io.IOException;
import com.google.sps.data.Comment;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.*;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  /**
   *This welcome String will remain here for now as it is still 
   *in one of my PRs and I am unsure as to what the best practice 
   *is for these cases. In the meantime, this String will be sent as
   *part of the JSON response.
  */
  private String welcome = "Welcome to my portfolio!";

  // An array list containing all the data that will be recieved with a Get request.
  private ArrayList<Object[]> data;
  
  /**
   * A linked list that will store every comment on the site.
   *
   * A linked list is used instead of an ArrayList in order to ensure that a deletion 
   * implementation occurs faster in cases where the comment to be deleted is in the
   * middle of the list.
   */
  private LinkedList<Comment> comments = new LinkedList<Comment>();;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Initialize Linked List/s
    data = new ArrayList<Object[]>();
    buildIntroData();
    buildCommentData();

    // Get comment data from the Datastore
    Query query = new Query("cmt");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Create the linked list object from the data.
    createList(results);
    
    // Convert the List/s to JSON
    String json = convertToJson(data);

    // Send JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String username = request.getParameter("user-name");
    String usercomment = request.getParameter("user-comment");

    // Check to see if any of the responses are blank.
    if(username == "" || usercomment == "") {
      response.setContentType("text/html");
      response.getWriter().println("One of the fields are empty.");
      return;
    }

    // Create an Entity of the comment
    Entity msgEntity = new Entity("cmt");
    msgEntity.setProperty("author", username);
    msgEntity.setProperty("comment", usercomment);

    // Store the entity in the datastore.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(msgEntity);

    // Redirect back to the html page.
    response.sendRedirect("/index.html");
  }
  
  // Creates the linked list containing the comments using the Datastore.
  private  void createList(PreparedQuery results) {
    for (Entity entity : results.asIterable()) {
      String username = (String) entity.getProperty("author");
      String usercomment = (String) entity.getProperty("comment");
      Comment comment = new Comment(username, usercomment);
      addComment(comment);
    }
  }

  // Adds a comment into the Servlet's internal data.
  private void addComment(Comment comment) {
      comments.add(comment);
  }

  // Builds the intro-data Linked List.
  /**
   * I implemented the data this way in order to make it easier for me to extend this implementation to the rest of
   * the portfolio's pages. This way, If I feel like I have to,I can just have my data contain various arrays for 
   * the different pages/data I need to load onto the page.
   */
  private void buildIntroData() {
    String[] intro_page = new String[4];
    String intro_img_src = "/images/main.jpg";
    String intro_msg = "This page will serve as a repository of my relevant work in Computer Science and Education as well as a some fun things I do on the side!";
    String intro_btn_txt = "Learn More";
    String intro_btn_onclick = "swapHidden('welcome','intro-main')";
    intro_page[0] = intro_img_src;
    intro_page[1] = intro_msg;
    intro_page[2] = intro_btn_txt;
    intro_page[3] = welcome;
    data.add(intro_page);
  }

  // Builds the comment-data that will be stored in the LinkedList.
  private void buildCommentData() {
    // The comment data is first wrapped in an Object array of size one so that it can be added into the Object[] array list.
    Object[] wrap = new Object[1];
    wrap[0] = comments;
    data.add(wrap);
  }

  // Returns the JSON representation of the given Linked List.
  private String convertToJson(ArrayList<Object[]> data) {
    Gson gson = new Gson();
    String json = gson.toJson(data);
    return json;
  }
}
