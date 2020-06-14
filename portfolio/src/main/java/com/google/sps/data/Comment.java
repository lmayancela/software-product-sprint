package com.google.sps.data;

import java.util.*;

/**
 * Class representing user comment, where the name of the user and the comment is stored for use in the servlet.
 *
 * TODO:
 * - Store date and time of comment.
 * - Implement Edit feature.
 * - Implement replies.
 * 
 */
public class Comment {
    private String username;
    private String comment;

    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUser() {
        return username;
    }

    public String getComment() {
        return comment;
    }

}