package com.stackroute.to_do_authentication.controller;

import com.stackroute.to_do_authentication.exception.UserAlreadyExistsException;
import com.stackroute.to_do_authentication.exception.UserNotFoundException;
import com.stackroute.to_do_authentication.feignclient.SignupData;
import com.stackroute.to_do_authentication.model.User;
import com.stackroute.to_do_authentication.service.JwtTokenGenerator;
import com.stackroute.to_do_authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequestMapping("/to-do-auth-app")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    /*
    POST
    http://localhost:9999/to-do-auth-app/register-user
     */
    @PostMapping(value = "/register-user",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<?> registerUser(@RequestParam("emailId") String emailId,
                                           @RequestParam("password") String password,
                                           @RequestParam("name") String name,
                                           @RequestParam("gender") String gender,
                                           @RequestParam("dateOfBirth") String dateOfBirth,
                                           @RequestParam("profilePic") MultipartFile profilePic) throws UserAlreadyExistsException, IOException {
        try {
            String loc= System.getProperty("user.dir");

            SignupData signupData = new SignupData(emailId, password,name,gender,dateOfBirth);
            String dpPath = loc+"\\profile_pics\\"+emailId+".jpg";
            profilePic.transferTo(new File(dpPath));
            return new ResponseEntity<>(userService.registerUser(signupData), HttpStatus.OK);
        } catch (UserAlreadyExistsException ex) {
            return new ResponseEntity<>("User Already exists",HttpStatus.OK);
        }

    }

    /*
    POST
    http://localhost:9999/to-do-auth-app/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginCheck(@RequestBody User user) throws UserNotFoundException {
        User result = userService.login(user.getEmailId(),user.getPassword());
        System.out.println(result);
        if(result!=null){
            result.setPassword("");
            return new ResponseEntity<>(jwtTokenGenerator.generateJwt(result),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Login failed",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    POST
    http://localhost:9999/to-do-auth-app/resetPassword
     */
    @PostMapping("/resetPassword/{currentPassword}")
    public ResponseEntity<?> resetPassword(@RequestBody User user,@PathVariable String currentPassword){
        try {
            return new ResponseEntity<>(userService.resetPassword(user,currentPassword), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>("Wrong Credentials",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    POST
    http://localhost:9999/to-do-auth-app/forgotPassword
     */
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> resetPassword(@RequestBody User user){
        try {
            return new ResponseEntity<>(userService.forgotPassword(user), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>("Wrong Credentials",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

