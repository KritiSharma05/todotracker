package com.stackroute.to_do_authentication.service;

import com.stackroute.to_do_authentication.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service
public class JwtTokenGeneratorImpl implements JwtTokenGenerator{
    @Override
    public Map<String, String> generateJwt(User user) {
        Map<String, String> result = new HashMap<String,String>();
        //build token(header,payload,by compacting)
        //user has emailId,password
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("emailid",user.getEmailId());


        String jwt = Jwts.builder()
                .setClaims(userdata) //user data to be filled as claims
                .setIssuedAt(new Date())
                .setIssuer("MyCompany")
                .signWith(SignatureAlgorithm.HS512,"secretKey")
                .compact();
        result.put("token",jwt);

        result.put("message","Login success,Token generated");
        return result;
        //return map : token(key,val),message(key,val)
    }
}
