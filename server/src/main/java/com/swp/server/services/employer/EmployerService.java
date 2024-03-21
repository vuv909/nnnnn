package com.swp.server.services.employer;

import com.swp.server.dto.ViewProfileDTO;
import org.springframework.http.ResponseEntity;

public interface EmployerService {
    public ResponseEntity<?> viewJobSeekerProfile(ViewProfileDTO viewProfileDTO);
}
