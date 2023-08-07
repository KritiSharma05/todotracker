package com.stackroute.to_do_authentication.feignclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupData {
    private String emailId, password, name, gender, dateOfBirth;
}
