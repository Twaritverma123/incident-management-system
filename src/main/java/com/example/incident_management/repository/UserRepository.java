package com.example.incident_management.repository;

import com.example.incident_management.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

}
