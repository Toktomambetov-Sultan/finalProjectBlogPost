package org.ucentralasia.photoApp.service;

import org.ucentralasia.photoApp.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto getAddress(String addressId);
    List<AddressDto> getAddresses(String userId);
}