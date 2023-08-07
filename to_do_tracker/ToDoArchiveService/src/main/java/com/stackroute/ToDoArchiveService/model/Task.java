package com.stackroute.ToDoArchiveService.model;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document
public class Task {

    @Transient
    public static final String SEQUENCE_NAME = "task_sequence";


    private int taskId;
    private String taskHeading;
    private String taskCategory;
    private String taskDescription;
    private String taskEndDate;
    private String taskStartDate;
    private boolean status;
    private Priority priority;

    private boolean hasImage;

}
