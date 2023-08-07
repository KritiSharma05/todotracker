package com.stackroute.ToDoArchiveService.service;

import com.stackroute.ToDoArchiveService.exception.TaskNotFoundException;
import com.stackroute.ToDoArchiveService.exception.UserNotFoundException;
import com.stackroute.ToDoArchiveService.model.Task;
import com.stackroute.ToDoArchiveService.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserArchieveService {

    User unArchiveTask(Task task, String userEmailId) throws UserNotFoundException, TaskNotFoundException;
    User shiftToArchieveList(Task task, String userEmailId) throws UserNotFoundException;
    public List<Task> getAllArchieveTask(String userEmailId) throws UserNotFoundException;
    User deleteArchivedTask(int taskId, String userEmailId) throws UserNotFoundException, TaskNotFoundException;

    public List<Task> getAllDeletedTask(String userEmailId) throws UserNotFoundException;

}
