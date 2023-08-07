package com.stackroute.ToDo.service.service;

import com.stackroute.ToDo.service.exception.TaskAlreadyExistException;
import com.stackroute.ToDo.service.exception.TaskNotFoundException;
import com.stackroute.ToDo.service.exception.UserAlreadyRegistered;
import com.stackroute.ToDo.service.exception.UserNotFoundException;
import com.stackroute.ToDo.service.model.Task;
import com.stackroute.ToDo.service.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public interface To_Do_Service {
    User registerUser(User user)throws UserAlreadyRegistered;
    User updateProfile(String userEmailId, User user1) throws UserNotFoundException;

    String getUserName(String userEmailId) throws UserNotFoundException;

    User getUserDetails(String userEmailId) throws UserNotFoundException;
    Task addTaskToUser(Task task, String userEmailId) throws UserNotFoundException, TaskAlreadyExistException, TaskNotFoundException;
    User reShiftToUserTask(Task task, String userEmailId, int taskId) throws UserNotFoundException, TaskAlreadyExistException, TaskNotFoundException;
//    User shiftToArchieveList(Task task, String userEmailId,int taskId) throws UserNotFoundException, TaskAlreadyExistException, TaskNotFoundException;
    User updateTask(Task task,String userEmailId) throws UserNotFoundException, TaskNotFoundException;
    User completeTask(Task task, String userEmailId) throws UserNotFoundException, TaskNotFoundException;
    User removeTaskToRecycle(Task task, String userEmailId, int taskId) throws UserNotFoundException, TaskNotFoundException;
//    public List<Task> getAllArchieveTask(String userEmailId) throws UserNotFoundException;
//    User restoreTask(Task task, String userEmailId, int taskId) throws UserNotFoundException, TaskNotFoundException;
//    User deleteArTask(int taskId, String userEmailId) throws UserNotFoundException, TaskNotFoundException;
//    User deleteDlTask(int taskId, String userEmailId) throws UserNotFoundException, TaskNotFoundException;
    List<Task> getAllTask(String userEmailId) throws UserNotFoundException;
    List<Task> getPendingTask(String userEmailId) throws TaskNotFoundException;
    List<Task> getCompletedTask(String userEmailId) throws TaskNotFoundException;

//    public List<Task> getAllDeletedTask(String userEmailId) throws UserNotFoundException;
    List<Task> categorizeByStartDate(String userEmail) throws UserNotFoundException;
    List<Task> categorizeByEndDate(String userEmailId) throws UserNotFoundException;
    List<Task> categorizeByPriority(String userEmailId) throws UserNotFoundException;
    Task getTask(String userEmailId, int taskId) throws TaskNotFoundException, UserNotFoundException;
    List<Task> getTasksWithNearDueDate(String userEmail) throws UserNotFoundException,ParseException;

    public List<Task> getTasksWithOverDue(String userEmail) throws UserNotFoundException, ParseException;
    List<Task> getTodayTask(String userEmail) throws UserNotFoundException,ParseException;

    String feedbackForm(String userEmail,String message);

    Boolean updateProfilePic(String userEmail, MultipartFile profilePic) throws UserNotFoundException, IOException;


    byte[] getProfilePic(String userEmailId) throws UserNotFoundException, IOException;

    Boolean addImageToATask(int taskId, MultipartFile taskImage, String userEmailId) throws UserNotFoundException, IOException;

    byte[] getTaskImage(int taskId) throws IOException;
}
