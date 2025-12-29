package com.github.victhorcm.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.victhorcm.auth.model.User;
import com.github.victhorcm.auth.services.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessLogInterceptor implements HandlerInterceptor {

    @Autowired
    private LogService logService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        
      
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

     
        if (auth != null && auth.getPrincipal() instanceof User user) {
            
           
            String ip = request.getRemoteAddr();
            String endpoint = request.getRequestURI();
            String method = request.getMethod();

           
            logService.saveLog(user, ip, endpoint, method);
        }
    }
}