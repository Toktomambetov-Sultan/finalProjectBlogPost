package org.ucentralasia.photoApp.service;

import org.ucentralasia.photoApp.shared.dto.PostDto;

import java.util.List;

public interface PostService{
    PostDto createPost(PostDto postDto);

    PostDto getPostByPostId(String postId);

    List<PostDto> getPostsByUserId(String userId);
    PostDto updatePost(String postId, PostDto post);

    List<PostDto> getPosts();

    void deletePostByPostId(String postId);
}