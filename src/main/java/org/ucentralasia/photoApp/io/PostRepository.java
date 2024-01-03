package org.ucentralasia.photoApp.io;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ucentralasia.photoApp.io.entity.PostEntity;
import org.ucentralasia.photoApp.io.entity.UserEntity;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {
    Iterable<PostEntity> findAllByUserDetails(UserEntity userEntity);
    PostEntity findByPostId(String postId);
}
