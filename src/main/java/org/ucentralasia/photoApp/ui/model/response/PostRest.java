package org.ucentralasia.photoApp.ui.model.response;

import org.joda.time.DateTime;

public class PostRest {
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    private String postId;
    private String title;
    private String content;
    private String createdDate;

    public UserRest getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserRest userDetails) {
        this.userDetails = userDetails;
    }

    private UserRest userDetails;


}
