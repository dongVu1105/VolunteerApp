package com.dongVu1105.profile_service.repository;

import com.dongVu1105.profile_service.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    UserProfile findByUserId (String userId);
    Page<UserProfile> findAllByUserIdIn (List<String> userId, Pageable pageable);
}
