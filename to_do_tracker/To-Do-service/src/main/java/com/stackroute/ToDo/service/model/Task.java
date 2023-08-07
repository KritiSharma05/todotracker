package com.stackroute.ToDo.service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Task {
    @Transient
    public static final String SEQUENCE_NAME="task_sequence";

    @Id
    private int taskId;
    private String taskHeading ;
    private String taskCategory;
    private String taskDescription;
    private String taskEndDate;
    private String taskStartDate;
    private boolean status;
    private Priority priority;

    private boolean hasImage;

    }
