package com.stackroute.to_do_authentication.service;

import com.stackroute.to_do_authentication.exception.UserAlreadyExistsException;
import com.stackroute.to_do_authentication.exception.UserNotFoundException;
import com.stackroute.to_do_authentication.feignclient.SignupData;
import com.stackroute.to_do_authentication.feignclient.UserDto;
import com.stackroute.to_do_authentication.feignclient.UserProxy;
import com.stackroute.to_do_authentication.model.User;
import com.stackroute.to_do_authentication.rabbitmq.EmailDTO;
import com.stackroute.to_do_authentication.rabbitmq.MailProducer;
import com.stackroute.to_do_authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProxy userProxy;
    @Autowired
    private MailProducer mailProducer;

    @Override
    public User registerUser(SignupData signUpData) throws UserAlreadyExistsException {

        if(userRepository.findById(signUpData.getEmailId()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        User user = new User(signUpData.getEmailId(), signUpData.getPassword());

        User result = userRepository.save(user);

        UserDto userDto = new UserDto(signUpData.getEmailId(),signUpData.getName(), signUpData.getGender(),signUpData.getDateOfBirth());
        ResponseEntity re= null;
        try {
            re = userProxy.sendUserDtoToToDoApp(userDto);
        } catch (Exception e) {
            userRepository.deleteById(signUpData.getEmailId());
            throw e;
        }
        System.out.println(re);
        //fill user details to user object from signUpdata, call repository.save() :mysql
        // todo send mail notification: async request to mail application
        EmailDTO emailDTO = new EmailDTO(signUpData.getEmailId(), "Welcome to To-Do Tracker Application", "Signup is success");
        mailProducer.sendEmailDtoQueue(emailDTO);
        return result;
    }

    @Override
    public User login(String userId, String password) {
        User userLogin= userRepository.findByEmailIdAndPassword(userId,password);
        return userLogin;
    }

    @Override
    public User resetPassword(User user, String currentPassword) throws UserNotFoundException {
        User userResetPwd= userRepository.findByEmailIdAndPassword(user.getEmailId(),currentPassword);
        if(userResetPwd!=null){
            userResetPwd.setPassword(user.getPassword());
            userRepository.save(userResetPwd);
            return userResetPwd;

        }
        throw new UserNotFoundException();
    }

    @Override
    public boolean forgotPassword(User user) throws UserNotFoundException {
        User userDetails= userRepository.findById(user.getEmailId()).get();
        if(userDetails!=null){
            EmailDTO emailDTO = new EmailDTO(userDetails.getEmailId(), "Your password is "+userDetails.getPassword(), "Password Recovery");
            mailProducer.sendEmailDtoQueue(emailDTO);
            return true;

        }
        throw new UserNotFoundException();
    }
}
