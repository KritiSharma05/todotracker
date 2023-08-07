package com.stackroute.ToDoArchiveService.rabbitMq;

import com.stackroute.ToDoArchiveService.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetails {
    private int taskId;
    private String taskHeading ;
    private String taskCategory;
    private String taskDescription;
    private Date taskEndDate;
    private Date taskStartDate;
    private boolean status;
    private String priority;
    private Priority emailId;
}

