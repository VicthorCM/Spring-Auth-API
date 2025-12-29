package com.github.victhorcm.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.victhorcm.auth.model.AccessLog;

public interface AccessLogRespository extends JpaRepository<AccessLog, Long>{


    

}
