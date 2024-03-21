package com.swp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swp.server.entities.Branch;
import com.swp.server.entities.Job_Application;

public interface BranchRepo extends JpaRepository<Branch, Integer> {

}
