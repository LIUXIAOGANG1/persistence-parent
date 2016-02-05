package com.github.lxgang.persistence.mongodb.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.github.lxgang.persistence.beans.User;

public interface UserRepository extends MongoRepository<User, Integer>{

}
