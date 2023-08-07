package com.stackroute.ToDo.service.service;

import com.stackroute.ToDo.service.exception.TaskAlreadyExistException;
import com.stackroute.ToDo.service.exception.TaskNotFoundException;
import com.stackroute.ToDo.service.exception.UserAlreadyRegistered;
import com.stackroute.ToDo.service.exception.UserNotFoundException;
import com.stackroute.ToDo.service.model.Feedback;
import com.stackroute.ToDo.service.model.Task;
import com.stackroute.ToDo.service.model.User;
import com.stackroute.ToDo.service.proxy.UserArchiveProxy;
import com.stackroute.ToDo.service.proxy.UserAuthProxy;
import com.stackroute.ToDo.service.repository.FeedbackRepository;
import com.stackroute.ToDo.service.repository.UserToDoServiceRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class To_Do_ServiceImpl implements To_Do_Service {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserToDoServiceRepository repository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserAuthProxy authProxy;

    @Autowired
    private UserArchiveProxy archieveProxy;

    @Autowired
    private SequenceGenerator sequenceGenerator;




    @Override
    public User registerUser(User user) throws UserAlreadyRegistered {
        System.out.println("Now check");
        if (repository.findById(user.getUserEmailId()).isPresent()) {
            throw new UserAlreadyRegistered();
        }
        return repository.save(user);
    }

    @Override
    public User updateProfile(String userEmailId, User user) throws UserNotFoundException {
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User userObj = repository.findByUserEmailId(userEmailId);
        if (!StringUtils.isEmpty(user.getUserName()))
            userObj.setUserName(user.getUserName());
        if (!StringUtils.isEmpty(user.getDateOfBirth()))
            userObj.setDateOfBirth(user.getDateOfBirth());
        if (!StringUtils.isEmpty(user.getUserGender()))
            userObj.setUserGender(user.getUserGender());
        repository.save(userObj);
        emailService.updateProfile(user);
        System.out.println("User Profile Has Been Updated");
        return userObj;
    }

    @Override
    public String getUserName(String userEmailId) throws UserNotFoundException {
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = repository.findById(userEmailId).get();
        return user.getUserName();
    }

    @Override
    public User getUserDetails(String userEmailId) throws UserNotFoundException {
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user=repository.findByUserEmailId(userEmailId);
        return user;

    }

    @Override
    public Task addTaskToUser(Task task, String userEmailId) throws UserNotFoundException, TaskAlreadyExistException {
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User userObject = repository.findByUserEmailId(userEmailId);
        System.out.println("in said add task in service" + userObject);
        task.setTaskId(sequenceGenerator.SequenceNumber(Task.SEQUENCE_NAME));
        List<Task> tasks;
        if (userObject.getUserTask() == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = userObject.getUserTask();
            for (Task task1 : tasks) {
                if (task1.getTaskHeading().equalsIgnoreCase(task.getTaskHeading())) {
                    throw new TaskAlreadyExistException();
                }
            }
        }
        tasks.add(task);
        userObject.setUserTask(tasks);
        repository.save(userObject);
        return task;
    }

    @Override
    public User updateTask(Task updatedTask, String userEmailId) throws UserNotFoundException, TaskNotFoundException {
        System.out.println("I m in Service impl");
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = repository.findById(userEmailId).get();
        List<Task> tasks = user.getUserTask();
        Task currentTask = null;
        for (Task t : tasks) {
            if (updatedTask.getTaskId() == t.getTaskId()) {
                currentTask = t;
                System.out.println("Task matched");
                break;
            }
        }
        if (currentTask == null) {
            throw new TaskNotFoundException();
        } else {
            user.getUserTask().remove(currentTask);
            if(StringUtils.isNotEmpty(updatedTask.getTaskDescription()))
                currentTask.setTaskDescription(updatedTask.getTaskDescription());
            tasks.add(updatedTask);
            user.setUserTask(tasks);
            repository.save(user);
        }
        return repository.save(user);
    }

    @Override
    public User completeTask(Task task, String userEmailId) throws UserNotFoundException, TaskNotFoundException {
        System.out.println("Check to remove");
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User userObj=repository.findByUserEmailId(userEmailId);
        List<Task> userTask=userObj.getUserTask();

        if(userTask.isEmpty())
        {
            throw new TaskNotFoundException();
        }
        for (Task t:userTask) {
            if(t.getTaskId()== task.getTaskId())
            {
                t.setStatus(true);
                return repository.save(userObj);

            }
        }
        return repository.save(userObj);
    }

    @Override
    public User removeTaskToRecycle(Task task, String userEmailId, int taskId) throws UserNotFoundException, TaskNotFoundException {
        System.out.println("check for archive add ");
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User userObj=repository.findByUserEmailId(userEmailId);
        List<Task> userTask=userObj.getUserTask();
        System.out.println(userTask);

        if(userTask==null)
        {
            throw new TaskNotFoundException();
        }
        for (Task t:userTask) {
            System.out.println(t.getTaskId());
            System.out.println("CHeck if Condition");
            System.out.println(t.getTaskId()== taskId);
            if(t.getTaskId()== taskId)
            {
                userTask.remove(t);
                System.out.println("removed by list");


                System.out.println("added to recycle");
                return repository.save(userObj);
            }
        }
        return repository.save(userObj);
    }


    @Override
    public User reShiftToUserTask(Task task, String userEmailId, int taskId) throws UserNotFoundException, TaskAlreadyExistException, TaskNotFoundException {
        System.out.println("check for archive add ");
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User userObj=repository.findByUserEmailId(userEmailId);
//        List<Task> archieTask=userObj.getArchieveTask();

        List<Task> tasks;
        if (userObj.getUserTask() == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = userObj.getUserTask();
            for (Task task1 : tasks) {
                if (task1.getTaskHeading().equalsIgnoreCase(task.getTaskHeading())) {
                    throw new TaskAlreadyExistException();
                }
            }
        }
        tasks.add(task);
        userObj.setUserTask(tasks);
        return repository.save(userObj);
    }


    @Override
    public List<Task> getAllTask(String userEmailId) throws UserNotFoundException {
        User user = repository.findById(userEmailId).get();
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        else {
            if(user.getUserTask()==null)
            {
                System.out.println("User Task list Is Empty");
            }
            return user.getUserTask();
        }
    }

    @Override
    public List<Task> getPendingTask(String userEmailId) throws TaskNotFoundException {

        return repository.findById(userEmailId).get().getUserTask().stream().filter(t->!t.isStatus()).collect(Collectors.toList());
    }

    @Override
    public List<Task> getCompletedTask(String userEmailId) throws TaskNotFoundException{

        return repository.findById(userEmailId).get().getUserTask().stream().filter(t->t.isStatus()).collect(Collectors.toList());
    }


    @Override
    public List<Task> categorizeByStartDate(String userEmailId) throws UserNotFoundException {
        if (repository.existsById(userEmailId)) {
            return repository.findById(userEmailId).get().getUserTask()
                    .stream().sorted(Comparator.comparing(Task::getTaskStartDate)).collect(Collectors.toList());

        }
        throw new UserNotFoundException();
    }

    @Override
    public List<Task> categorizeByEndDate(String userEmailId) throws UserNotFoundException {
        if (repository.existsById(userEmailId)) {
            return repository.findById(userEmailId).get().getUserTask()
                    .stream().sorted(Comparator.comparing(Task::getTaskEndDate)).collect(Collectors.toList());
        }
        throw new UserNotFoundException();
    }

    @Override
    public List<Task> categorizeByPriority(String userEmailId) throws UserNotFoundException {
        System.out.println("i m in Priority");
        if (repository.existsById(userEmailId)) {
            System.out.println("Id Checked ");
            return repository.findById(userEmailId).get().getUserTask()
                    .stream().sorted(Comparator.comparing(Task::getPriority)).collect(Collectors.toList());
        }
        throw new UserNotFoundException();
    }

    @Override
    public Task getTask(String userEmailId, int taskId) throws TaskNotFoundException, UserNotFoundException {
        if (repository.findById(userEmailId).isPresent()) {
            User user = repository.findById(userEmailId).get();
            List<Task> tasks = user.getUserTask();
            Task task2 = null;
            for (Task task1 : tasks) {
                if (task1.getTaskId() == taskId) {
                    task2 = task1;
                    break;
                }
            }
            if (task2 == null) {
                throw new TaskNotFoundException();
            }
            return task2;
        } else {
            throw new UserNotFoundException();
        }
    }



    Comparator<Task> dueDateComparator = ((task1, task2) -> {
//        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate dueDate1= null;
        try {
            dueDate1 = new SimpleDateFormat("dd/MM/yyyy").parse(task1.getTaskEndDate()).toInstant()
                       .atZone(ZoneId.systemDefault())
                     .toLocalDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("***********************************"+dueDate1);
        System.out.println(task1);
        System.out.println(task2);

        LocalDate dueDate2 = null;
        try {
            dueDate2 = new SimpleDateFormat("dd/MM/yyyy").parse(task2.getTaskEndDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*****************************************"+dueDate2);
        if (dueDate1.isBefore(dueDate2)) {
            return -1;
        }
        if (dueDate1.isAfter(dueDate2)) {
            return 1;
        }
        return 0;
    });

    @Override
    public List<Task> getTasksWithNearDueDate(String userEmailId) throws UserNotFoundException,ParseException {
        String localdate= String.valueOf(LocalDate.now());
        User u=repository.findByUserEmailId(userEmailId);
        List<Task> list=u.getUserTask();
        List<Task> enddaytask = new ArrayList<>();
        for(Task x:list){


            Date end=new SimpleDateFormat("dd/MM/yyyy").parse(x.getTaskEndDate());
            Instant instant=end.toInstant();
            ZonedDateTime z=instant.atZone(ZoneId.systemDefault());
            System.out.println("end day==="+z.toLocalDate());

            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(x.getTaskStartDate());
            Instant instan=start.toInstant();
            ZonedDateTime z1=instan.atZone(ZoneId.systemDefault());
            System.out.println("start day==="+z1.toLocalDate());


            String endday= String.valueOf(z.toLocalDate());
            String startday= String.valueOf(z1.toLocalDate());

            System.out.println(localdate+"  ==  "+z.toLocalDate());
            if(localdate.equals(endday) == true){
                enddaytask.add(x);

            }
        }

        return enddaytask;
    }

    @Override
    public List<Task> getTodayTask (String userEmailId) throws UserNotFoundException,ParseException {
        String localdate= String.valueOf(LocalDate.now());
        User u=repository.findByUserEmailId(userEmailId);
        List<Task> list=u.getUserTask();
        List<Task> enddaytask = new ArrayList<>();
        for(Task x:list){


            Date end=new Date();
            Instant instant=end.toInstant();
            ZonedDateTime z=instant.atZone(ZoneId.systemDefault());
//            System.out.println("end day==="+z.toLocalDate());
//
//            Date start=new SimpleDateFormat("dd/MM/yyyy").parse(x.getTaskStartDate());
//            Instant instan=start.toInstant();
//            ZonedDateTime z1=instan.atZone(ZoneId.systemDefault());
//            System.out.println("start day==="+z1.toLocalDate());


            String endday= String.valueOf(z.toLocalDate());
//            String startday= String.valueOf(z1.toLocalDate());

//            System.out.println(localdate+"  ==  "+z.toLocalDate());
            if(x.getTaskEndDate().equals(endday)){
                enddaytask.add(x);

            }

        }

        return enddaytask;
    }

    @Override
    public String feedbackForm(String userEmail, String message) {
        System.out.println("Give Feed Back");
        Feedback feedback = new Feedback();
        feedback.setMessage(message);
        feedback.setFeedBackBy(userEmail);
        feedbackRepository.insert(feedback);
        emailService.feedBack(userEmail);
        emailService.feedBackToAdmin(message,userEmail);
        return "Feedback Submitted";
    }

    @Override
    public List<Task> getTasksWithOverDue(String userEmail) throws UserNotFoundException,ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Optional<User> user = repository.findById(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<Task> allTasks = user.get().getUserTask();
        List<Task> tasksOverDue = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.isStatus()) {
                continue;
            }
            Date dueDate =new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(new Date(task.getTaskEndDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()));
            if (dueDate.before(new Date())) {
                tasksOverDue.add(task);
            }
        }
        return tasksOverDue.stream().sorted(dueDateComparator).collect(Collectors.toList());
    }

    @Override
    public Boolean updateProfilePic(String userEmail, MultipartFile profilePic) throws UserNotFoundException, IOException {
        if (repository.existsById(userEmail)) {
            String loc= System.getProperty("user.dir");
            String dpPath = loc+"\\profile_pics\\"+userEmail+".jpg";
            profilePic.transferTo(new File(dpPath));
            return true;
        }
        throw new UserNotFoundException();
    }

    @Override
    public byte[] getProfilePic(String userEmailId) throws UserNotFoundException, IOException {
        if (repository.findById(userEmailId).isPresent()) {
            String loc= System.getProperty("user.dir");
            String dpPath = loc+"\\profile_pics\\"+userEmailId+".jpg";
            File f=new File(dpPath);
            if(!f.exists()){
                f=new File(loc+"\\profile_pics\\default.jpg");
            }
            BufferedImage o=ImageIO.read(f);
            ByteArrayOutputStream b=new ByteArrayOutputStream();
            ImageIO.write(o, "jpg", b);
            byte[] img=b.toByteArray();

            return img;
        }
        throw new UserNotFoundException();
    }

    @Override
    public Boolean addImageToATask(int taskId, MultipartFile taskImage, String userEmailId) throws UserNotFoundException, IOException {
        if (repository.findById(userEmailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        String loc= System.getProperty("user.dir");
        String imgPath = loc+"\\task_images\\"+taskId+".jpg";
        taskImage.transferTo(new File(imgPath));

        User userObj=repository.findByUserEmailId(userEmailId);
        List<Task> userTask=userObj.getUserTask();
        for (Task t:userTask) {
            if(t.getTaskId()==taskId)
            {
                t.setHasImage(true);
                repository.save(userObj);
                return true;

            }
        }
        return false;
    }

    @Override
    public byte[] getTaskImage(int taskId) throws IOException {
        String loc= System.getProperty("user.dir");
        String dpPath = loc+"\\task_images\\"+taskId+".jpg";
        File f=new File(dpPath);
        if(!f.exists()){
            f=new File(loc+"\\task_images\\task-icon.jpg");

        }
        BufferedImage o=ImageIO.read(f);
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        ImageIO.write(o, "jpg", b);
        byte[] img=b.toByteArray();

        return img;
    }

}