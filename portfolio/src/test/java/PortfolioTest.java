package com.google.sps.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.sps.data.Comment;
import com.google.sps.servlets.DataServlet;
import com.google.gson.Gson;
import com.google.sps.servlets.TranslationServlet;
// import java.util.String;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public final class PortfolioTest {

  @Test
  // Tests the functionality of the Comment class.
  public void testComment() {
    Comment comment = new Comment("Lisandro","Test Comment");

    String author = comment.getUser();
    String message = comment.getComment();

    Assert.assertEquals(author, "Lisandro");
    Assert.assertEquals(message, "Test Comment");
  }

  @Test
  // Tests the internal data List of a servlet to ensure data is stored correctly.
  public void testDataServletList() {
      // Initialize the data List.
      DataServlet servlet = new DataServlet();
      ArrayList<Object[]> data = servlet.initData();

      // Add a test comment to the data.
      Comment comment = new Comment("Lisandro","Test Comment");
      servlet.addComment(comment);

      // Convert the data to json and assert that the String is not empty or 'null'
      String json = servlet.convertToJson(data);
      Assert.assertNotEquals(json, "");
      Assert.assertNotEquals(json, null);
  }
}