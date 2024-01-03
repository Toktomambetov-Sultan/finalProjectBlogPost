package org.ucentralasia.photoApp.service.impl;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucentralasia.photoApp.io.UserRepository;
import org.ucentralasia.photoApp.io.entity.PostEntity;
import org.ucentralasia.photoApp.io.PostRepository;
import org.ucentralasia.photoApp.io.entity.UserEntity;
import org.ucentralasia.photoApp.service.PostService;
import org.ucentralasia.photoApp.shared.Utils;
import org.ucentralasia.photoApp.shared.dto.AddressDto;
import org.ucentralasia.photoApp.shared.dto.PostDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    Utils utils;
    @Override
    public PostDto createPost(PostDto postDto) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity author = userRepository.findByUserId(postDto.getUserId());
        if (author == null) {
            throw new RuntimeException("Author doesn't exists");
        }
        PostEntity postEntity = modelMapper.map(postDto,PostEntity.class);
        postEntity.setUserDetails(author);
        postEntity.setPostId(utils.generateUserId(30));
        postEntity.setCreatedDate(DateTime.now().toString());
        PostEntity storedPost = postRepository.save(postEntity);
        postDto = modelMapper.map(storedPost,PostDto.class);
        return postDto;
    }

    @Override
    public PostDto getPostByPostId(String postId) {
        ModelMapper modelMapper = new ModelMapper();
        PostDto postDto = null;
        PostEntity returnValue=  postRepository.findByPostId(postId);
        if(returnValue != null){
            postDto = modelMapper.map(returnValue,PostDto.class);
        }
        return postDto;
    }

    @Override
    public List<PostDto> getPostsByUserId(String userId) {
        ModelMapper modelMapper = new ModelMapper();
        List<PostDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return returnValue;
        }

        Iterable<PostEntity> addresses = postRepository.findAllByUserDetails(userEntity);
        for (PostEntity address : addresses) {
            returnValue.add(modelMapper.map(address, PostDto.class));
        }

        return returnValue;
    }

    @Override
    public PostDto updatePost(String postId, PostDto post) {
        ModelMapper modelMapper = new ModelMapper();
        PostEntity postEntity = postRepository.findByPostId(postId);
        if(postEntity == null){
            throw new RuntimeException("Post doesn't exist");
        }
        UserEntity userEntity = userRepository.findByUserId(post.getUserId());
        if(userEntity == null){
            throw new RuntimeException("User doesn't exist");
        }
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setUserDetails(userEntity);
        PostEntity updatedEntity = postRepository.save(postEntity);
        post = modelMapper.map(updatedEntity,PostDto.class);
        return post;
    }

    @Override
    public List<PostDto> getPosts() {
        ModelMapper modelMapper = new ModelMapper();
        List<PostDto> returnValue = new ArrayList<>();

        Iterable<PostEntity> posts = postRepository.findAll();

        for (PostEntity userEntity : posts) {
            returnValue.add(modelMapper.map(userEntity, PostDto.class));
        }
        return returnValue;
    }

    @Override
    public void deletePostByPostId(String postId) {
        PostEntity post =postRepository.findByPostId(postId);
        if(post == null){
            return;
        }
        postRepository.delete(post);
    }

}
