package com.stackroute.to_do_email.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {
    private String receiver, messageBody, subject,attachment;

}
