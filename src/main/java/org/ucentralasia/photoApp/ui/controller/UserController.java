package org.ucentralasia.photoApp.ui.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.ucentralasia.photoApp.exceptions.UserServiceExceptions;
import org.ucentralasia.photoApp.service.AddressService;
import org.ucentralasia.photoApp.service.PostService;
import org.ucentralasia.photoApp.service.UserService;
import org.ucentralasia.photoApp.shared.dto.AddressDto;
import org.ucentralasia.photoApp.shared.dto.PostDto;
import org.ucentralasia.photoApp.shared.dto.UserDto;
import org.ucentralasia.photoApp.ui.model.request.UserDetailsRequestModel;
import org.ucentralasia.photoApp.ui.model.response.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilderDslKt.withRel;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    PostService postService;


    // http://localhost:8080/context-path/users/userId
    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    // POST: http://localhost:8080/context-path/users + payload
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue;

        if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() ||
                userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
            // throw new NullPointerException("The object is null"); - for testing purposes
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    // PUT: http://localhost:8080/context-path/users/userId + payload
    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    // DELETE: http://localhost:8080/context-path/users/userId
    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        userService.deleteUser(id);
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    // GET: http://localhost:8080/context-path/users?page=0&limit25
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    // GET: http://localhost:8080/context-path/users/userId/addresses
    @GetMapping(path = "/{userId}/posts", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<PostRest> getUserAddresses(@PathVariable String userId) {
        ModelMapper modelMapper = new ModelMapper();
        List<PostRest> returnValue = new ArrayList<>();
        List<PostDto> addressDtos = postService.getPostsByUserId(userId);
        for (PostDto  postDto: addressDtos) {
            returnValue.add(modelMapper.map(postDto,PostRest.class));
        }
        return returnValue;
    }

    // GET: http://localhost:8080/context-path/users/userId/addresses/addressId
    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressDto addressDto = addressService.getAddress(addressId);

        AddressesRest returnValue = new ModelMapper().map(addressDto, AddressesRest.class);

        // Implementing HATEOAS
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        Link userAddressesLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
                .withRel("addresses");

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId))
                .withSelfRel();

        return EntityModel.of(returnValue, Arrays.asList(userLink, userAddressesLink, selfLink));
    }

    // GET: http://localhost:8080/context-path/users/email-verification?token=secret-token-value
    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token", defaultValue = "") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified == true) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }
}



























