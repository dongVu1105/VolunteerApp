package com.dongVu1105.post_service.repository;

import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.entity.React;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactRepository extends MongoRepository<React, String> {
    Page<React> findAllByPostId (String postId, Pageable pageable);
    boolean existsByPostIdAndOwnerId (String postId, String ownerId);
}
