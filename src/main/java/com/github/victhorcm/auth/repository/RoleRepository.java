package com.github.victhorcm.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.victhorcm.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

   Optional<Role> findByName(String name);
}
