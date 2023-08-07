package com.stackroute.ToDo.service.proxy;


import com.stackroute.ToDo.service.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "To-Do-Auth-Service", url = "localhost:8001")
public interface UserAuthProxy {
    @PostMapping("/api/to-do/auth/registerUser")
    public ResponseEntity<?> saveuserdeatilstodatabase(@RequestBody User user);
    @PutMapping("api/to-do/auth/updateProfile/{userEmailId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String userEmailId,@RequestBody User user);

}