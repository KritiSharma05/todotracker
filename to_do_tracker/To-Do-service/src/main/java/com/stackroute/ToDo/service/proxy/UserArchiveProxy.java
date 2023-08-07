package com.stackroute.ToDo.service.proxy;

import com.stackroute.ToDo.service.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "To-Do-Archieve-Service")
public interface UserArchiveProxy {
    @PostMapping("/api/to-do/archieveService/registerUserInArchieve")
    ResponseEntity<?> registerUser(@RequestBody User user);

}
