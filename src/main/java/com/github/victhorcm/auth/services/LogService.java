package com.github.victhorcm.auth.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.victhorcm.auth.model.AccessLog;
import com.github.victhorcm.auth.model.User;
import com.github.victhorcm.auth.repository.AccessLogRespository;

@Service
public class LogService {


    @Autowired
    private AccessLogRespository repository;


    public void saveLog(User user, String ip, String endpoint, String method){

        AccessLog log = new AccessLog();
        log.setUser(user);
        log.setEndpoint(method+" / "+endpoint);
        log.setIpAdress(ip);
        log.setTimestamp(LocalDateTime.now());

        repository.save(log);
       
    }

}
