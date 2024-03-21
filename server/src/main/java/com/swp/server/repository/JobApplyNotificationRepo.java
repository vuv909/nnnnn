package com.swp.server.repository;

import com.swp.server.dto.SendJobApplyNotificationDTO;
import com.swp.server.entities.JobApplyNotification;
import com.swp.server.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplyNotificationRepo extends JpaRepository<JobApplyNotification,Integer> {
    List<JobApplyNotification> findAllByEmail(String email);
}
