package com.google.sps.servlets;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/translate")
public class TranslationServlet extends HttpServlet {
  
  // This array list will hold all of the translated comments
  private ArrayList<String> newComments;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Initialize the array list.
    newComments = new ArrayList<String>();

    // Get the request parameters.
    String languageCode = request.getParameter("languageCode");
    String[] comments = request.getParameterValues("comments");


    // Do the translation.
    for(int i=0; i<comments.length; i++) {
      String originalComment = comments[i];
      Translate translate = TranslateOptions.getDefaultInstance().getService();
      Translation translation =
        translate.translate(originalComment, Translate.TranslateOption.targetLanguage(languageCode));
      String translatedText = translation.getTranslatedText();
      newComments.add(translatedText);
    }
   
    // Convert the List to JSON
    String json = convertToJson(newComments);

    // Send JSON as the response
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().println(json);
  }

  // Returns the JSON representation of the given List List.
  private String convertToJson(ArrayList<String> data) {
    Gson gson = new Gson();
    String json = gson.toJson(data);
    return json;
  }
}
