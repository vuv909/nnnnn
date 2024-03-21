package com.swp.server.controller;

import com.swp.server.dto.ViewProfileDTO;
import com.swp.server.services.employer.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/employer")
public class EmployerController {
    @Autowired
    private EmployerService employerService;

    @GetMapping("hello")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("hllo");
    }
    @PostMapping("profileJobseeker")
    public ResponseEntity<?> viewJobseekerProfile(@RequestBody ViewProfileDTO viewProfileDTO){
        return employerService.viewJobSeekerProfile(viewProfileDTO);
    }
}
