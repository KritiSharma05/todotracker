package com.stackroute.ToDo.service.controller;

import com.stackroute.ToDo.service.exception.TaskAlreadyExistException;
import com.stackroute.ToDo.service.exception.TaskNotFoundException;
import com.stackroute.ToDo.service.exception.UserAlreadyRegistered;
import com.stackroute.ToDo.service.exception.UserNotFoundException;
import com.stackroute.ToDo.service.model.Task;
import com.stackroute.ToDo.service.model.User;
import com.stackroute.ToDo.service.service.To_Do_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/to-do-service")
public class ToDoController {

    @Autowired
    private To_Do_Service service;

    ResponseEntity responseEntity = null;


    @PostMapping("/registerUser")
    ResponseEntity<?>  registerUser(@RequestBody User user)
    {
        System.out.println(user);
        try
        {
            System.out.println("check controller");
            responseEntity=new ResponseEntity(service.registerUser(user), HttpStatus.ACCEPTED);
        }
        catch (UserAlreadyRegistered e)
        {
            responseEntity=new ResponseEntity("User Already Exist ",HttpStatus.CONFLICT);
        }
        catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Error Pls Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/feedback/{message}")
    ResponseEntity<?>  feedbackForm(HttpServletRequest request,@PathVariable String message)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            System.out.println("check controller for FeedBack");
            Map<String,String> res = new HashMap<String,String>();
            res.put("msg",service.feedbackForm(userEmailId, message));
            responseEntity=new ResponseEntity(res, HttpStatus.OK);
        }
        catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Error Pls Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }



    @GetMapping("/getUserName")
    ResponseEntity<?> getUserName (HttpServletRequest request) throws UserNotFoundException
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.getUserName(userEmailId),HttpStatus.OK);
        }catch (UserNotFoundException u)
        {
            responseEntity=new ResponseEntity("User Not Found Exception",HttpStatus.NOT_FOUND);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("User Having Error With Url pls... Check",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }



    @GetMapping("/getUserDetails")
    ResponseEntity<?> getUserDetail(HttpServletRequest request) throws UserNotFoundException
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.getUserDetails(userEmailId),HttpStatus.OK);
        }catch (UserNotFoundException u)
        {
            responseEntity=new ResponseEntity("User Not Found Exception",HttpStatus.NOT_FOUND);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("User Having Error With Url pls... Check",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }




    @PutMapping("/updateProfile")
    ResponseEntity<?> updateProfile(HttpServletRequest request,@RequestBody User user)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");

        try
        {
            responseEntity=new ResponseEntity(service.updateProfile(userEmailId, user), HttpStatus.ACCEPTED);
        } catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Error Pls Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @PostMapping("/addTask")
    ResponseEntity<?> addTaskToUser(@RequestBody Task task,HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.addTaskToUser(task, userEmailId),HttpStatus.CREATED);

        } catch (TaskAlreadyExistException e) {
            responseEntity=new ResponseEntity("TAsk Already Saved In list",HttpStatus.CONFLICT);

        } catch (UserNotFoundException | TaskNotFoundException e) {
           responseEntity=new ResponseEntity("USer Not Found Exception",HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @PutMapping("/updateTask")
    ResponseEntity<?>updateTask(@RequestBody Task task,HttpServletRequest request){
        String userEmailId=(String)request.getAttribute("current_user_emailId");
    try
    {
        responseEntity=new ResponseEntity(service.updateTask(task, userEmailId),HttpStatus.CREATED);
    }catch (Exception e)
    {
        responseEntity=new ResponseEntity("Server Error Pls Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
    }
        return responseEntity;
    }
    //1

    @PostMapping(value = "/add-image-to-task/{taskId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<?> addImageToATask(@RequestParam("taskImage") MultipartFile taskImage,
                                              @PathVariable int taskId,
                                              HttpServletRequest request) throws  IOException {

        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            return new ResponseEntity<>(service.addImageToATask(taskId,taskImage,userEmailId), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>("User Not Found",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //1

    @GetMapping(
            value = "/get-task-image/{taskId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<?> getTaskImage(@PathVariable int taskId) throws IOException {
        return new ResponseEntity<>(service.getTaskImage(taskId), HttpStatus.OK);
    }

    @PutMapping("/CompleteTask")
    ResponseEntity<?>completeTask(@RequestBody Task task,HttpServletRequest request){
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.completeTask(task, userEmailId),HttpStatus.CREATED);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Error Pls Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PutMapping("/remove/{taskId}")
    ResponseEntity<?>removeTask(@RequestBody Task task,HttpServletRequest request,@PathVariable int taskId){
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        System.out.println("Remove Controller ");
        try
        {
            responseEntity=new ResponseEntity(service.removeTaskToRecycle(task,userEmailId,taskId),HttpStatus.OK);
            System.out.println("shifted");
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Error Pls Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PutMapping("/reShift/{taskId}")
    ResponseEntity<?> reShiftTask(@RequestBody Task task,HttpServletRequest request,@PathVariable int taskId)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        System.out.println("Working");
        try
        {
            responseEntity=new ResponseEntity(service.reShiftToUserTask(task, userEmailId, taskId),HttpStatus.ACCEPTED);
        }catch (UserNotFoundException u)
        {
            responseEntity=new ResponseEntity("User Not Found ",HttpStatus.NOT_FOUND);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Internal server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/CompletedTask")
    ResponseEntity<?> getAllDeletedTask(HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.getCompletedTask(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/allTask")
    ResponseEntity<?> getAllTask(HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.getAllTask(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/allPendingTask")
    ResponseEntity<?> getPendingTask(HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.getPendingTask(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/startDate")
    ResponseEntity<?>categorizeByStartDate(HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.categorizeByStartDate(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/priority")
    ResponseEntity<?> categorizeByPriority(HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        System.out.println("Check Controller for priority");
        try
        {
            System.out.println("try section");
            responseEntity=new ResponseEntity(service.categorizeByPriority(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/endDate")
    ResponseEntity<?> categorizeByEndDate(HttpServletRequest request)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.categorizeByEndDate(userEmailId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/getTask/{taskId}")
    ResponseEntity<?> getTask(HttpServletRequest request,@PathVariable int taskId)
    {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try
        {
            responseEntity=new ResponseEntity(service.getTask(userEmailId,taskId),HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity=new ResponseEntity("Server Not Found ....Try Again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @GetMapping("/taskNearDue")
    public ResponseEntity<?> getTasksWithNearDueDate(HttpServletRequest request) throws UserNotFoundException {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try {
            return new ResponseEntity<>(service.getTasksWithNearDueDate(userEmailId), HttpStatus.OK);
        } catch(UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("try after some time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todayTask")
    public ResponseEntity<?> getTodayTask(HttpServletRequest request) throws UserNotFoundException {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try {
            return new ResponseEntity<>(service.getTodayTask(userEmailId), HttpStatus.OK);
        } catch(UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("try after some time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //1

    @PostMapping(value = "/update-profile-pic",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<?> updateProfilePic(@RequestParam("profilePic") MultipartFile profilePic,
                                           HttpServletRequest request) throws  IOException {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try {
            return new ResponseEntity<>(service.updateProfilePic(userEmailId,profilePic), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>("User Not Found",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(
            value = "/get-profile-pic",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<?> getImageWithMediaType(HttpServletRequest request) throws IOException {
        String userEmailId=(String)request.getAttribute("current_user_emailId");
        try {
            return new ResponseEntity<>(service.getProfilePic(userEmailId), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>("User Not Found",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
