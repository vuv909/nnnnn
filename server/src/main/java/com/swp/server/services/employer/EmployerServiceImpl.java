package com.swp.server.services.employer;

import com.swp.server.dto.ProfileDTO;
import com.swp.server.dto.ResponseProfileDTO;
import com.swp.server.dto.ViewProfileDTO;
import com.swp.server.entities.Account;
import com.swp.server.entities.Profile;
import com.swp.server.enums.AccountRole;
import com.swp.server.repository.AccountRepo;
import com.swp.server.repository.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployerServiceImpl implements EmployerService{

    @Autowired
    private ProfileRepo profileRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Override
    public ResponseEntity<?> viewJobSeekerProfile(ViewProfileDTO viewProfileDTO) {
        try{
            Optional<Account> checkEmailExisted = accountRepo.findFirstByEmail(viewProfileDTO.getEmail());
            if (!checkEmailExisted.isPresent()) {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "Email is not existed  !!!");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            Account account = checkEmailExisted.get();
            if(!account.getRole().getName().equals(AccountRole.JOBSEEKER.toString())){
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "Request is not accepted  !!!");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            Profile profile = account.getProfile();
            if (profile == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Profile not found!");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            ResponseProfileDTO profileResponse = getResponseProfileDTO(account, profile);
            return ResponseEntity.ok(profileResponse);
        }catch (Exception e){
            Map<String, Object> error = new HashMap<>();
            error.put("error", "INTERNAL SERVER ERROR !!!");
            return new ResponseEntity<>(error, HttpStatus.ACCEPTED);
        }
    }

    private static ResponseProfileDTO getResponseProfileDTO(Account a, Profile profile) {
        ResponseProfileDTO profileResponse = new ResponseProfileDTO();

        profileResponse.setRole(a.getRole().getName());
        profileResponse.setUsername(a.getUsername());
        profileResponse.setVerify(a.isVerify());
        profileResponse.setEnabled(a.isEnabled());

        profileResponse.setAddress(profile.getAddress());
        profileResponse.setEmail(a.getEmail());
        profileResponse.setFirstName(profile.getFirstName());
        profileResponse.setLastName(profile.getLastName());
        profileResponse.setPhoneNumber(profile.getPhoneNumber());
        profileResponse.setGender(profile.isGender());
        profileResponse.setAvatar(profile.getAvatar());
        return profileResponse;
    }
}
