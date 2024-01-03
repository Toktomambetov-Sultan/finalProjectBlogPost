package org.ucentralasia.photoApp.io;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ucentralasia.photoApp.io.entity.UserEntity;
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
    Page<UserEntity> findAll(Pageable pageable);
    UserEntity findUserByEmailVerificationToken(String token);
}