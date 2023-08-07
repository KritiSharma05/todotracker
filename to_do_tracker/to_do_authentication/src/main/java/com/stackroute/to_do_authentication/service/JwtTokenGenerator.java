package com.stackroute.to_do_authentication.service;

import com.stackroute.to_do_authentication.model.User;

import java.util.Map;

public interface JwtTokenGenerator {
    public abstract Map<String,String> generateJwt(User user);

}
