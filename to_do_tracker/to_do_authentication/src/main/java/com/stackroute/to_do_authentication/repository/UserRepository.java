package com.stackroute.to_do_authentication.repository;

import com.stackroute.to_do_authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    //select * from user where emailId=? and password=?
    public abstract User findByEmailIdAndPassword(String emailId, String password);

}
