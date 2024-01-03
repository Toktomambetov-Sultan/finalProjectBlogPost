package org.ucentralasia.photoApp.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.ucentralasia.photoApp.exceptions.UserServiceExceptions;
import org.ucentralasia.photoApp.io.UserRepository;
import org.ucentralasia.photoApp.io.entity.UserEntity;
import org.ucentralasia.photoApp.service.UserService;
import org.ucentralasia.photoApp.shared.AmazonSES;
import org.ucentralasia.photoApp.shared.Utils;
import org.ucentralasia.photoApp.shared.dto.AddressDto;
import org.ucentralasia.photoApp.shared.dto.UserDto;
import org.ucentralasia.photoApp.ui.model.response.ErrorMessages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDto getUser(String email) {
        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UserServiceExceptions(ErrorMessages.EMAIL_ADDRESS_NOT_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        // check whether the user already exist to prevent duplicate records
        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());
        if (storedUserDetails != null) {
            throw new RuntimeException("Record already exists");
        }

        // setting entity relationships
        for (int i = 0; i < userDto.getAddresses().size(); i++) {
            AddressDto address = userDto.getAddresses().get(i);
            address.setUserDetails(userDto);
            address.setAddressId(utils.generateAddressId(30));
            userDto.getAddresses().set(i, address);
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        // BeanUtils.copyProperties(userDto, userEntity);

        String publicUserID = utils.generateUserId(30);
        userEntity.setUserId(publicUserID);
        // temporary setting values that must be generated by us
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userEntity.setEmailVerificationToken(Utils.generateEmailVerificationToken(publicUserID));
        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUser = userRepository.save(userEntity);

        // BeanUtils.copyProperties(storedUser, returnValue);
        UserDto returnValue = modelMapper.map(storedUser, UserDto.class);

        //send and email message to user to verify their email address
        new AmazonSES().verifyEmail(returnValue);

        return returnValue;
    }
    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        UserEntity updatedEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedEntity, returnValue);

        return returnValue;
    }
    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) {
            throw new UsernameNotFoundException(id);
        }
        userRepository.delete(userEntity);
    }
    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }
        return returnValue;
    }
    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;
        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hasTokenExpired = Utils.hasTokenExpired(token);
            if (!hasTokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }
        return returnValue;
    }
    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        // return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                userEntity.getEmailVerificationStatus(),
                true,true, true, new ArrayList<>());
    }
}







































