package org.ucentralasia.photoApp.shared.dto;

import org.joda.time.DateTime;

import java.io.Serializable;

public class PostDto implements Serializable {
    private static final long serialVersionUID = 931037080458294132L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDto getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDto userDetails) {
        this.userDetails = userDetails;
    }

    private Long id;
    private String title;
    private String content;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    private String postId;
    private String userId;
    private String createdDate;
    private UserDto userDetails;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
