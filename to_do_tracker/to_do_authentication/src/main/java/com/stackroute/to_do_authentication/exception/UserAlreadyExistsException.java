package com.stackroute.to_do_authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "User Already exists")
public class UserAlreadyExistsException extends Exception{

}
