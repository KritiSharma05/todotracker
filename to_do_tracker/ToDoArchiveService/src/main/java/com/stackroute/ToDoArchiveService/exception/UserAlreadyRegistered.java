package com.stackroute.ToDoArchiveService.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT,reason = "User Already Registered")
public class UserAlreadyRegistered extends Exception {
}
