package com.stackroute.ToDo.service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class Feedback {
    @Id
    private String id;
    private String feedBackBy;
    private String message;
}

