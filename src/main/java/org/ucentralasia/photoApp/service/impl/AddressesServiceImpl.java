package org.ucentralasia.photoApp.service.impl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucentralasia.photoApp.io.AddressRepository;
import org.ucentralasia.photoApp.io.UserRepository;
import org.ucentralasia.photoApp.io.entity.AddressEntity;
import org.ucentralasia.photoApp.io.entity.UserEntity;
import org.ucentralasia.photoApp.service.AddressService;
import org.ucentralasia.photoApp.shared.dto.AddressDto;

import java.util.ArrayList;
import java.util.List;
@Service
public class AddressesServiceImpl implements AddressService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Override
    public List<AddressDto> getAddresses(String id) {
        ModelMapper modelMapper = new ModelMapper();
        List<AddressDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) {
            return returnValue;
        }

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for (AddressEntity address : addresses) {
            returnValue.add(modelMapper.map(address, AddressDto.class));
        }

        return returnValue;
    }
    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto returnValue = null;
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity != null) {
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
        }
        return returnValue;
    }
}