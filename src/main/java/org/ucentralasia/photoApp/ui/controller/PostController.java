package org.ucentralasia.photoApp.ui.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.ucentralasia.photoApp.exceptions.UserServiceExceptions;
import org.ucentralasia.photoApp.service.PostService;
import org.ucentralasia.photoApp.shared.dto.PostDto;
import org.ucentralasia.photoApp.ui.model.request.PostRequestModel;
import org.ucentralasia.photoApp.ui.model.response.ErrorMessages;
import org.ucentralasia.photoApp.ui.model.response.PostRest;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public PostRest createPost(@RequestBody PostRequestModel postDetails) throws Exception {
        PostRest returnValue;

        if (postDetails.getTitle().isEmpty() || postDetails.getContent().isEmpty() ||
                postDetails.getUserId().isEmpty()) {
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();

        PostDto postDto = modelMapper.map(postDetails, PostDto.class);

        PostDto createdPost = postService.createPost(postDto);

        returnValue = modelMapper.map(createdPost, PostRest.class);

        return returnValue;
    }
    @PatchMapping(path = "/{postId}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public PostRest updatePost(@PathVariable String postId, @RequestBody PostRequestModel postDetails) throws Exception {
       ModelMapper modelMapper = new ModelMapper();
       PostDto postDto = modelMapper.map(postDetails,PostDto.class);
       postDto = postService.updatePost(postId,postDto);
       PostRest returnValue = modelMapper.map(postDto,PostRest.class);
       return returnValue;
    }
    @GetMapping(path = "/{postId}")
    public PostRest getPostById (@PathVariable String postId){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(postService.getPostByPostId(postId),PostRest.class);
    }
    @DeleteMapping(path = "/{postId}")
    public String deletePostById (@PathVariable String postId){
        ModelMapper modelMapper = new ModelMapper();
        postService.deletePostByPostId(postId);
        return "deleted";
    }
    @GetMapping
    public List<PostRest> getAll (){
        ModelMapper modelMapper = new ModelMapper();
        List<PostRest> returnValue = new ArrayList<>();
        List<PostDto> postDtos = postService.getPosts();
        for (PostDto  postDto: postDtos) {

            returnValue.add(modelMapper.map(postDto,PostRest.class));
        }
        return returnValue;
    }


}
