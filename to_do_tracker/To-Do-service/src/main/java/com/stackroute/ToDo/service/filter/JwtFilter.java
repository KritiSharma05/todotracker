package com.stackroute.ToDo.service.filter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader("authorization");

        if(authHeader ==null|| !authHeader.startsWith("Bearer")){
            throw new ServletException("Token is missing");
        }
        else{

            String token = authHeader.substring(7);
            System.out.println("\n In Filter class, Token: " + token);
            Claims claims = Jwts.parser().setSigningKey("secretKey").parseClaimsJws(token).getBody();
            System.out.println("\n In Filter,claims : " + claims);
            request.setAttribute("current_user_emailId",claims.get("emailid"));
            filterChain.doFilter(request,response); //forwarding
        }

    }
}
