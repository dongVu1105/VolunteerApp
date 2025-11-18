package com.dongVu1105.post_service.repository;

import com.dongVu1105.post_service.entity.ChildReact;
import com.dongVu1105.post_service.entity.React;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildReactRepository extends MongoRepository<ChildReact, String> {
    Page<ChildReact> findAllByCommentId (String commentId, Pageable pageable);
    boolean existsByCommentIdAndOwnerId (String commentId, String ownerId);
    long countByCommentId(String commentId);

}
