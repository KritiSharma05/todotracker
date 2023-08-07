package com.stackroute.to_do_authentication.feignclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userEmailId;
    private String userName;
    private String userGender;
    private String dateOfBirth;
}
