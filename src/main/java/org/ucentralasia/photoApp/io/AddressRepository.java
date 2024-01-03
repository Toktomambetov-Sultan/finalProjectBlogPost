package org.ucentralasia.photoApp.io;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ucentralasia.photoApp.io.entity.AddressEntity;
import org.ucentralasia.photoApp.io.entity.UserEntity;
@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    Iterable<AddressEntity> findAllByUserDetails(UserEntity userEntity);
    AddressEntity findByAddressId(String addressId);
}
