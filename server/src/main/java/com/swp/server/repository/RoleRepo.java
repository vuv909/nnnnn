package com.swp.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swp.server.entities.Account;
import com.swp.server.entities.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

	Role findByName(String string);

	Role findById(int roleId);
}
