package com.stackroute.ToDoArchiveService.rabbitMq.rabbitConfig;

import com.stackroute.ToDoArchiveService.exception.TaskAlreadyExistException;
import com.stackroute.ToDoArchiveService.exception.UserNotFoundException;
import com.stackroute.ToDoArchiveService.rabbitMq.TaskDetails;
import com.stackroute.ToDoArchiveService.service.UserArchieveServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRabbitMq {
    @Autowired
    private UserArchieveServiceImpl archiveService;

    @RabbitListener(queues = "task_queue")
    public void getCustomerData( TaskDetails details) throws TaskAlreadyExistException, UserNotFoundException {
//        Task task=new Task(details.getTaskId(), details.getTaskHeading(),details.getTaskCategory(),details.getTaskDescription(),details.getTaskEndDate(),details.getTaskStartDate(),details.isStatus(),details.getPriority());
//        archiveService.addTask(task,details.getEmailId());
    }
}
