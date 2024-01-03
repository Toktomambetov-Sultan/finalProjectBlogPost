package org.ucentralasia.photoApp.io.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;


@Entity(name="posts")
public class PostEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1168618162649577078L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

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

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }

    @Column(length = 30, nullable = false)
    private String postId;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String content;



    @Column(nullable=false)
    @CreatedDate
    private String createdDate;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
