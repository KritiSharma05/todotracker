package com.stackroute.ToDoArchiveService.controller;

import com.stackroute.ToDoArchiveService.exception.TaskAlreadyExistException;
import com.stackroute.ToDoArchiveService.exception.TaskNotFoundException;
import com.stackroute.ToDoArchiveService.exception.UserNotFoundException;
import com.stackroute.ToDoArchiveService.model.Task;
import com.stackroute.ToDoArchiveService.service.UserArchieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("/to-do-archieveService")
public class ArchieveController {

    private UserArchieveService service;
    ResponseEntity responseEntity;

    @Autowired
    public ArchieveController(UserArchieveService service) {
        this.service = service;
    }


    @PostMapping("/archiveTask/{userEmailId}")
    ResponseEntity<?> archiveTask(@RequestBody Task task, @PathVariable String userEmailId) throws UserNotFoundException, TaskAlreadyExistException {
        System.out.println("User archieve service Working For Add Data");
        try {
            responseEntity = new ResponseEntity<>(service.shiftToArchieveList(task, userEmailId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>("try after some time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/unArchiveTask/{userEmailId}")
    ResponseEntity<?> unArchiveTask(@RequestBody Task task, @PathVariable String userEmailId) throws UserNotFoundException, TaskAlreadyExistException {

        try {
            responseEntity = new ResponseEntity<>(service.unArchiveTask(task, userEmailId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>("try after some time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @GetMapping("/getAllArchivedTask/{userEmailId}")
    ResponseEntity<?> getAllArchivedTask(@PathVariable String userEmailId)
    {
        try
        {
            responseEntity=new ResponseEntity(service.getAllArchieveTask(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/getAllDeletedTask/{userEmailId}")
    ResponseEntity<?> getAllDeletedTask(@PathVariable String userEmailId)
    {
        try
        {
            responseEntity=new ResponseEntity(service.getAllDeletedTask(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @DeleteMapping("/delete/{userEmailId}/{taskId}")
    ResponseEntity <?> DeleteTask(@PathVariable String userEmailId ,@PathVariable int taskId) throws  UserNotFoundException, TaskNotFoundException
    {
        {
            responseEntity =new ResponseEntity(service.deleteArchivedTask(taskId,userEmailId),HttpStatus.OK);
        }
        return responseEntity;
    }



}

