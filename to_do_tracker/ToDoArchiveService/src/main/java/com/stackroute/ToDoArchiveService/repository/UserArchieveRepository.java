package com.stackroute.ToDoArchiveService.repository;

import com.stackroute.ToDoArchiveService.exception.UserNotFoundException;
import com.stackroute.ToDoArchiveService.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserArchieveRepository extends MongoRepository<User,String> {

    User findByUserEmailId(String userEmailId) throws UserNotFoundException;

//    User findByUserEmailIdAndUserName(String userName,String userEmailId) throws UserNotFoundException;

}
