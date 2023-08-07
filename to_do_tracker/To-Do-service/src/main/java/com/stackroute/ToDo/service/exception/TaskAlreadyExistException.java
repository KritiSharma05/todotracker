package com.stackroute.ToDo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "User Already Added This Task")
public class TaskAlreadyExistException extends Exception{
}
