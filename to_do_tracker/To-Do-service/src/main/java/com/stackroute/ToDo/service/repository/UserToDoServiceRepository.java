package com.stackroute.ToDo.service.repository;

import com.stackroute.ToDo.service.exception.UserNotFoundException;
import com.stackroute.ToDo.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserToDoServiceRepository extends MongoRepository<User,String> {

    User findByUserEmailId(String userEmailId) throws UserNotFoundException;
    User findByUserEmailIdAndUserName(String userName,String UserEmailId) throws UserNotFoundException;
}
