package com.swp.server.repository;

import com.swp.server.entities.Job_Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryRepo extends JpaRepository<Job_Category, Integer> {
}
