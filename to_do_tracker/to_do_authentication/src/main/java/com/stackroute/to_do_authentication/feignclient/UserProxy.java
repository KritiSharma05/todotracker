package com.stackroute.to_do_authentication.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="to-do-app",url="localhost:8003")
public interface UserProxy {
    @PostMapping("/to-do-service/registerUser")
    public abstract ResponseEntity<?> sendUserDtoToToDoApp(@RequestBody UserDto userDto);
}
