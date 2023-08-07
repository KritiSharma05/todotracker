package com.stackroute.ToDoArchiveService.service;

import com.stackroute.ToDoArchiveService.exception.TaskNotFoundException;
import com.stackroute.ToDoArchiveService.exception.UserAlreadyRegistered;
import com.stackroute.ToDoArchiveService.exception.UserNotFoundException;
import com.stackroute.ToDoArchiveService.model.Task;
import com.stackroute.ToDoArchiveService.model.User;
import com.stackroute.ToDoArchiveService.repository.UserArchieveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserArchieveServiceImpl implements UserArchieveService{

    private UserArchieveRepository repository;
    private SequenceGenerator sequenceGenerator;
    @Autowired
    public UserArchieveServiceImpl(UserArchieveRepository repository, SequenceGenerator sequenceGenerator) {
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
    }


    @Override
    public User shiftToArchieveList(Task task, String userEmailId) throws UserNotFoundException {
        System.out.println("check for archive add ");
        if (repository.findById(userEmailId).isEmpty()) {
            User user = new User();
            user.setUserEmailId(userEmailId);
            repository.insert(user);
        }
        User userObj=repository.findByUserEmailId(userEmailId);
        List<Task> archievedTask=userObj.getArchievedTask();
        if(archievedTask == null)
            archievedTask = new ArrayList<>();

        System.out.println(archievedTask);
        for (Task t:archievedTask) {
            if(t.getTaskId()== task.getTaskId())
            {
                archievedTask.remove(t);
                System.out.println("removed duplicate FROM list");
            }
        }
        archievedTask.add(task);
        userObj.setArchievedTask(archievedTask);
        return repository.save(userObj);
    }

    @Override
    public User unArchiveTask(Task task, String userEmailId) throws UserNotFoundException, TaskNotFoundException {
        System.out.println("check for archive add ");
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User userObj=repository.findByUserEmailId(userEmailId);
        List<Task> archieTask=userObj.getArchievedTask();
        if(archieTask==null)
        {
            throw new TaskNotFoundException();
        }
        for (Task t:archieTask) {
            if(t.getTaskId()== task.getTaskId())
            {
                archieTask.remove(t);
                System.out.println("removed by list");
                break;
            }
        }
        userObj.setArchievedTask(archieTask);
        return repository.save(userObj);
    }

    @Override
    public List<Task> getAllArchieveTask(String userEmailId) throws UserNotFoundException {
        User user = repository.findByUserEmailId(userEmailId);
        System.out.println(user.getArchievedTask());
        if (repository.findById(userEmailId).isEmpty()) {
            return new ArrayList<>();
        }
        else {
            if(user.getArchievedTask()==null)
            {
                System.out.println("User Archieve list Is Empty");
            }
            return user.getArchievedTask();
        }
    }

    @Override
    public List<Task> getAllDeletedTask(String userEmailId) throws UserNotFoundException {
        User user = repository.findByUserEmailId(userEmailId);
        System.out.println(user.getDeletedTask());
        if (repository.findById(userEmailId).isEmpty()) {
            return new ArrayList<>();
        }
        else {
            if(user.getDeletedTask()==null)
            {
                System.out.println("User ReCycle Bin  Is Empty");
            }
            return user.getDeletedTask();
        }
    }

//    delete task

    @Override
    public User deleteArchivedTask(int taskId, String userEmailId) throws UserNotFoundException {
        System.out.println("Delete ServiceImpl");
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User u = repository.findByUserEmailId(userEmailId);
        List<Task> archievedTask = u.getArchievedTask();
        List<Task> deletedTask = u.getDeletedTask();
        if (deletedTask == null)
            deletedTask = new ArrayList<>();
        System.out.println("user Find ");
        for (Task x : archievedTask) {
            if (taskId == x.getTaskId()) {
                System.out.println("Task find");
                archievedTask.remove(x);
                System.out.println("Task Removed");
                deletedTask.add(x);
                System.out.println("User task moved to deleted");
                break;
            }
        }
        u.setDeletedTask(deletedTask);
        u.setArchievedTask(archievedTask);
        return  repository.save(u);
    }

}

