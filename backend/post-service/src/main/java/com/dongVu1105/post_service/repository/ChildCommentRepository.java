package com.dongVu1105.post_service.repository;

import com.dongVu1105.post_service.entity.ChildComment;
import com.dongVu1105.post_service.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildCommentRepository extends MongoRepository<ChildComment, String> {
    Page<ChildComment> findAllByCommentId (String postId, Pageable pageable);
    long countByCommentId(String commentId);
}
