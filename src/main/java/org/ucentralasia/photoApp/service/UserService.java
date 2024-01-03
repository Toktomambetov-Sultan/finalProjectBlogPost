package org.ucentralasia.photoApp.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.ucentralasia.photoApp.shared.dto.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);
    UserDto getUserByUserId(String id);
    UserDto updateUser(String id, UserDto userDto);
    void deleteUser(String id);
    List<UserDto> getUsers(int page, int limit);
    boolean verifyEmailToken(String token);
}