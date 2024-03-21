package com.swp.server.job;

import com.swp.server.dto.*;
import com.swp.server.entities.Branch;
import com.swp.server.entities.Job;
import com.swp.server.entities.Job_Category;
import com.swp.server.repository.BranchRepo;
import com.swp.server.repository.JobCategoryRepo;
import com.swp.server.repository.JobRepo;
import com.swp.server.services.job.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JobServiceTest {

    @Mock
    private JobCategoryRepo jobCategoryRepo;

    @Mock
    private JobRepo jobRepo;

    @Mock
    private BranchRepo branchRepo;

    @InjectMocks
    private JobServiceImpl jobService; // Assuming JobService contains the createJob function

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSearchJobWithValidData() {
        int page = 1;
        SearchJobDtp searchJobDtp = new SearchJobDtp();
        searchJobDtp.setText(".NET/C# Developer");
        searchJobDtp.setCategory(1);
        searchJobDtp.setBranch(1);
        searchJobDtp.setCareer_level("Manager");
        searchJobDtp.setExperience(0);
        searchJobDtp.setSalary("$2000-$3000");
        searchJobDtp.setQualification("Bachelor Degree");

        List<Job> jobList = new ArrayList<>();
        Job job1 = new Job();
        job1.setId(1);
        job1.setName(".NET/C# Developer");
        job1.setDescription("Design and develop software applications.");
        job1.setAddress("1820 Hàng Trống");
        job1.setJob_Type("Full-time");
        job1.setHrEmail("hr@company.com");
        job1.setDeleted(false);
        job1.setApply_Before(new Date(2024-03-20));
        job1.setJob_category(jobCategoryRepo.getReferenceById(1));
        job1.setCareer_Level("Manager");
        job1.setExperience(0);
        job1.setOffer_Salary("$2000-$3000");
        job1.setQualification("Bachelor Degree");
        job1.setBranch(branchRepo.getReferenceById(1));
        jobList.add(job1);

        when(jobRepo.findByCriteria(anyString(), anyInt(), anyInt(), anyString(), anyInt(), anyString(), anyString()))
                .thenReturn(jobList);

        ResponseEntity<?> response = jobService.searchJob(searchJobDtp,1);
        assertEquals(ResponseEntity.ok(jobList), response);
    }

    @Test
    public void testSearchJobWithInvalidData() {
        SearchJobDtp searchJobDtp = new SearchJobDtp();
        searchJobDtp.setText("Invalid Job");
        searchJobDtp.setCategory(null);
        searchJobDtp.setBranch(null);
        searchJobDtp.setCareer_level(null);
        searchJobDtp.setExperience(null);
        searchJobDtp.setSalary(null);
        searchJobDtp.setQualification(null);

        when(jobRepo.findByCriteria(anyString(), anyInt(), anyInt(), anyString(), anyInt(), anyString(), anyString()))
                .thenReturn(new ArrayList<>());

        ResponseEntity<?> response = jobService.searchJob(searchJobDtp,1);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs found matching the search criteria."), response);
    }

    @Test
    public void testViewJobByCategoryIdWithValidData() {
        int categoryId = 1;

        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Software Engineer");
        job1.setDescription("Design and develop software applications.");
        job1.setAddress("123 Main St.");
        job1.setJob_Type("Full-time");
        job1.setHrEmail("hr@company.com");
        job1.setDeleted(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        job1.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));
        job1.setJob_category(jobCategoryRepo.getReferenceById(1));
        job1.setCareer_Level("Entry Level");
        job1.setExperience(0);
        job1.setOffer_Salary("$2000-$3000");
        job1.setQualification("Bachelor Degree");
        job1.setBranch(branchRepo.getReferenceById(1));
        List<Job> jobList = new ArrayList<>();
        jobList.add(job1);

        when(jobRepo.findAllByCategoryId(anyInt())).thenReturn(jobList);

        ResponseEntity<?> response = jobService.viewJobByCategoryId(categoryId);
        assertEquals(ResponseEntity.ok(List.of(getJobDTO(job1))), response);
    }

    @Test
    public void testViewJobByCategoryIdWithInvalidData() {
        int categoryId = 10;

        when(jobRepo.findAllByCategoryId(anyInt())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = jobService.viewJobByCategoryId(categoryId);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job is empty")), response);
    }

    private JobDTO getJobDTO(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setName(job.getName());
        jobDTO.setCategoryName(job.getJob_category().getName());
        jobDTO.setCareer_Level(job.getCareer_Level());
        jobDTO.setExperience(job.getExperience());
        jobDTO.setOffer_Salary(job.getOffer_Salary());
        jobDTO.setQualification(job.getQualification());
        jobDTO.setJob_Type(job.getJob_Type());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setApply_Before(job.getApply_Before());
        jobDTO.setHrEmail(job.getHrEmail());
        jobDTO.setAddress(job.getAddress());
        jobDTO.setUpdate_At(job.getUpdated_At());
        jobDTO.setCreate_At(job.getCreated_At());

        LocalDate applyBeforeDate = job.getApply_Before().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Check if the job is expired or valid based on applyBefore date
        if (LocalDate.now().isAfter(applyBeforeDate)) {
            jobDTO.setStatus("expired");
        } else {
            jobDTO.setStatus("valid");
        }

        return jobDTO;
    }

    @Test
    public void testCreateJobCategoryWithValidData() throws IOException {
        JobCategoryDTO jobDTO = new JobCategoryDTO();
        jobDTO.setName("Software Engineer");
        jobDTO.setImage(new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", "some bytes".getBytes()));

        when(jobCategoryRepo.save(any(Job_Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = jobService.createJobCategory(jobDTO);
        assertEquals(ResponseEntity.ok().body(Map.of("success", "Thêm thành công !!!")), response);
    }

    @Test
    public void testCreateJobCategoryWithInvalidData() {
        JobCategoryDTO jobDTO = new JobCategoryDTO();
        jobDTO.setName(null);
        jobDTO.setImage(null);

        ResponseEntity<?> response = jobService.createJobCategory(jobDTO);
        assertEquals(ResponseEntity.badRequest().body(Map.of("error", "Không được để trống trường nào trong job category !!!")), response);
    }

    @Test
    public void testViewByHrEmailWithValidData() {
        String email = "hr@company.com";
        Job_Category job_Category = new Job_Category();
        java.util.Date date = new java.util.Date();
        Date sqlDate = new Date(date.getTime());
        job_Category.setImage(null);
        job_Category.setName("Software Engineer");
        job_Category.setDeleted(false);
        job_Category.setCreated_At(sqlDate);

        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Software Engineer");
        job1.setDescription("Design and develop software applications.");
        job1.setAddress("123 Main St.");
        job1.setJob_Type("Full-time");
        job1.setHrEmail(email);
        job1.setDeleted(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        job1.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));
        job1.setJob_category(job_Category);
        job1.setCareer_Level("Entry Level");
        job1.setExperience(0);
        job1.setOffer_Salary("$2000-$3000");
        job1.setQualification("Bachelor's Degree");

        List<Job> jobList = new ArrayList<>();
        jobList.add(job1);

        when(jobRepo.findByHrEmail(anyString())).thenReturn(jobList);

        ResponseEntity<?> response = jobService.viewByHrEmail(email);
        assertEquals(ResponseEntity.ok(List.of(getJobDTO(job1))), response);
    }

    @Test
    public void testViewByHrEmailWithInvalidData() {
        String email = "invalid@email.com";

        when(jobRepo.findByHrEmail(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = jobService.viewByHrEmail(email);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job is empty")), response);
    }

    @Test
    public void testGetAllBranchWithValidData() {

        Branch branch1 = new Branch();
        branch1.setName("Branch 1");
        branch1.setAddress("Address 1");
        branch1.setImg("img1.jpg");

        Branch branch2 = new Branch();
        branch2.setName("Branch 2");
        branch2.setAddress("Address 2");
        branch2.setImg("img2.jpg");

        List<Branch> branchList = new ArrayList<>();
        branchList.add(branch1);
        branchList.add(branch2);

        when(branchRepo.findAll()).thenReturn(branchList);

        ResponseEntity<?> response = jobService.getAllBranch();
        assertEquals(ResponseEntity.ok(List.of(getBranchDTO(branch1), getBranchDTO(branch2))), response);
    }

    @Test
    public void testGetAllBranchWithInvalidData() {

        when(branchRepo.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = jobService.getAllBranch();
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Branch is empty")), response);
    }

    private BranchDTO getBranchDTO(Branch branch) {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setId(branch.getId());
        branchDTO.setName(branch.getName());
        branchDTO.setAddress(branch.getAddress());
        branchDTO.setImg(branch.getImg());
        return branchDTO;
    }

    @Test
    public void testUpdateJobCategoryWithValidData() throws IOException, SQLException {
        UpdateJobCategoryDTO updateJobCategoryDTO = new UpdateJobCategoryDTO();
        updateJobCategoryDTO.setId(1);
        updateJobCategoryDTO.setName("New Job Category");
        updateJobCategoryDTO.setImage(new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", "some bytes".getBytes()));

        Job_Category jobCategory = new Job_Category();
        jobCategory.setId(1);
        jobCategory.setName("Old Job Category");
        jobCategory.setImage(new byte[0]);

        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.of(jobCategory));
        when(jobCategoryRepo.save(any(Job_Category.class))).thenAnswer(invocation -> {
            Job_Category jobCategoryArgument = invocation.getArgument(0);
            jobCategoryArgument.setId(1);
            jobCategoryArgument.setName("New Job Category");
            jobCategoryArgument.setImage(new byte[0]); // Clear the image to simulate a successful update without image
            return jobCategoryArgument;
        });

        ResponseEntity<?> response = jobService.updateJobCategory(updateJobCategoryDTO);
        assertEquals(ResponseEntity.ok(Map.of("success", "Thêm thành công !!!")), response);
    }

    @Test
    public void testUpdateJobCategoryWithInvalidData() {
        UpdateJobCategoryDTO updateJobCategoryDTO = new UpdateJobCategoryDTO();
        updateJobCategoryDTO.setId(1);
        updateJobCategoryDTO.setName("New Job Category");
        updateJobCategoryDTO.setImage(new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", "some bytes".getBytes()));

        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = jobService.updateJobCategory(updateJobCategoryDTO);
        assertEquals(ResponseEntity.badRequest().body(Map.of("error", "Not exist !!!")), response);
    }

    @Test
    public void testDeleteJobCategoryWithValidData() {
        Job_Category jobCategory = new Job_Category();
        jobCategory.setId(1);
        jobCategory.setName("Job Category");
        jobCategory.setDeleted(false);

        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.of(jobCategory));
        doAnswer(invocation -> {
            Job_Category jobCategoryArgument = invocation.getArgument(0);
            jobCategoryArgument.setDeleted(true);
            return null;
        }).when(jobCategoryRepo).save(any(Job_Category.class));

        ResponseEntity<?> response = jobService.deleteJobCategory(1);

        assertEquals(ResponseEntity.ok(Map.of("success", "Xóa thành công !!!")), response);
        verify(jobCategoryRepo).save(jobCategory);
    }

    @Test
    public void testDeleteJobCategoryWithInvalidData() {
        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = jobService.deleteJobCategory(1);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Not exist !!!")), response);
        verify(jobCategoryRepo, never()).save(any(Job_Category.class));
    }

    @Test
    public void testCreateJobWithValidData() {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setName("Software Engineer");
        jobDTO.setCategory_Id(1);
        jobDTO.setBranch_Id(1);
        jobDTO.setCareer_Level("Manager");
        jobDTO.setExperience(5);
        jobDTO.setOffer_Salary("$2000-$3000");
        jobDTO.setQualification("Bachelor Degree");
        jobDTO.setJob_Type("Full-time");
        jobDTO.setDescription("Design and develop software applications.");
        jobDTO.setAddress("123 Main St.");
        jobDTO.setHrEmail("hr@company.com");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jobDTO.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));

        Branch branch = new Branch();
        branch.setId(1);
        branch.setName("Branch 1");
        branch.setAddress("123 Main St.");
        branch.setImg("branch1.jpg");

        Job_Category jobCategory = new Job_Category();
        jobCategory.setId(1);
        jobCategory.setName("Job Category 1");

        when(branchRepo.findById(anyInt())).thenReturn(Optional.of(branch));
        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.of(jobCategory));
        when(jobRepo.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = jobService.createJob(jobDTO);

        assertEquals(ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("success", "Job created successfully !!!" )), response);
        verify(jobRepo, times(1)).save(any(Job.class));
    }

    @Test
    public void testCreateJobWithInvalidData_branchNotFound() {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setName("Software Engineer");
        jobDTO.setCategory_Id(1);
        jobDTO.setBranch_Id(100); // Invalid branch ID
        jobDTO.setCareer_Level("Manager");
        jobDTO.setExperience(5);
        jobDTO.setOffer_Salary("$2000-$3000");
        jobDTO.setQualification("Bachelor Degree");
        jobDTO.setJob_Type("Full-time");
        jobDTO.setDescription("Design and develop software applications.");
        jobDTO.setAddress("123 Main St.");
        jobDTO.setHrEmail("hr@company.com");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jobDTO.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));

        when(branchRepo.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = jobService.createJob(jobDTO);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Not found branch")), response);
        verify(jobRepo, never()).save(any(Job.class));
    }

    @Test
    public void testCreateJobWithInvalidData_jobCategoryNotFound() {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setName("Software Engineer");
        jobDTO.setCategory_Id(100); // Invalid job category ID
        jobDTO.setBranch_Id(1);
        jobDTO.setCareer_Level("Manager");
        jobDTO.setExperience(5);
        jobDTO.setOffer_Salary("$2000-$3000");
        jobDTO.setQualification("Bachelor Degree");
        jobDTO.setJob_Type("Full-time");
        jobDTO.setDescription("Design and develop software applications.");
        jobDTO.setAddress("123 Main St.");
        jobDTO.setHrEmail("hr@company.com");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jobDTO.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));

        when(branchRepo.findById(anyInt())).thenReturn(Optional.of(new Branch()));
        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = jobService.createJob(jobDTO);

        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job category not found")), response);
        verify(jobRepo, never()).save(any(Job.class));
    }

    @Test
    public void testCreateJobWithInvalidData_negativeExperience() {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setName("Software Engineer");
        jobDTO.setCategory_Id(1);
        jobDTO.setBranch_Id(1);
        jobDTO.setCareer_Level("Manager");
        jobDTO.setExperience(-5); // Negative experience
        jobDTO.setOffer_Salary("$2000-$3000");
        jobDTO.setQualification("Bachelor Degree");
        jobDTO.setJob_Type("Full-time");
        jobDTO.setDescription("Design and develop software applications.");
        jobDTO.setAddress("123 Main St.");
        jobDTO.setHrEmail("hr@company.com");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jobDTO.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));

        when(branchRepo.findById(anyInt())).thenReturn(Optional.of(new Branch()));
        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.of(new Job_Category()));

        ResponseEntity<?> response = jobService.createJob(jobDTO);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Please input experience >= 0")), response);
        verify(jobRepo, never()).save(any(Job.class));
    }

    @Test
    public void testCreateJobWithInvalidData_invalidQualification() {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setName("Software Engineer");
        jobDTO.setCategory_Id(1);
        jobDTO.setBranch_Id(1);
        jobDTO.setCareer_Level("Manager");
        jobDTO.setExperience(5);
        jobDTO.setOffer_Salary("$2000-$3000");
        jobDTO.setQualification("High School Diploma"); // Invalid qualification
        jobDTO.setJob_Type("Full-time");
        jobDTO.setDescription("Design and develop software applications.");
        jobDTO.setAddress("123 Main St.");
        jobDTO.setHrEmail("hr@company.com");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jobDTO.setApply_Before(Date.valueOf(LocalDate.now().plusDays(10).format(formatter)));

        when(branchRepo.findById(anyInt())).thenReturn(Optional.of(new Branch()));
        when(jobCategoryRepo.findById(anyInt())).thenReturn(Optional.of(new Job_Category()));

        ResponseEntity<?> response = jobService.createJob(jobDTO);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Wrong input")), response);
        verify(jobRepo, never()).save(any(Job.class));
    }

    @Test
    public void testViewJob_invalidData() {
        // Prepare test data
        List<Job> jobs = new ArrayList<>();

        // Mock repository behavior
        when(jobRepo.findAll()).thenReturn(jobs);

        // Call method under test
        ResponseEntity<?> response = jobService.viewJob();

        // Verify response
        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> error = (Map<String, Object>) response.getBody();
        assertEquals("Job is empty", error.get("error"));
    }

    @Test
    public void testViewJobCategory_invalidData() {
        List<Job_Category> categories = new ArrayList<>();

        when(jobCategoryRepo.findAll()).thenReturn(categories);

        ResponseEntity<?> response = jobService.viewJobCategory();
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job category is empty")), response);
    }

    @Test
    public void updateJob_validData_success() {
        // Arrange
        Job_Category job_category = new Job_Category();
        job_category.setId(1);
        job_category.setName("Software Engineer");
        job_category.setDeleted(false);

        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(1);
        jobDTO.setCategory_Id(1);
        jobDTO.setCategoryName(job_category.getName());
        jobDTO.setName("Software Engineer");
        jobDTO.setJob_Type("Full Time");
        jobDTO.setDescription("We are looking for a software engineer");
        jobDTO.setApply_Before(Date.valueOf("2024-01-25"));
        jobDTO.setAddress("123 Main St, City, Country");
        jobDTO.setCareer_Level("Junior");
        jobDTO.setExperience(2);
        jobDTO.setOffer_Salary("$2000-$3000");
        jobDTO.setBranch_Id(1);
        jobDTO.setQualification("Bachelor Degree");

        Job job = new Job();
        job.setId(1);
        job.setName("Software Engineer");
        job.setJob_Type("Full Time");
        job.setDescription("We are looking for a software engineer");
        job.setApply_Before(Date.valueOf("2022-12-31"));
        job.setAddress("123 Main St, City, Country");
        job.setCareer_Level("Junior");
        job.setExperience(2);
        job.setOffer_Salary("$2000-$3000");
        job.setQualification("Bachelor Degree");

        Branch branch = new Branch();
        branch.setId(1);
        branch.setName("Head Office");
        branch.setAddress("123 Main St, City, Country");

        when(jobRepo.findById(1)).thenReturn(Optional.of(job));
        when(branchRepo.findById(1)).thenReturn(Optional.of(branch));
        when(jobCategoryRepo.findById(1)).thenReturn(Optional.of(job_category));
        when(jobRepo.save(any(Job.class))).thenReturn(job);

        // Act
        ResponseEntity<?> response = jobService.updateJob(jobDTO);

        // Assert
        assertEquals(ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("success", "Job updated successfully !!!" )), response);
        verify(jobRepo, times(1)).save(any(Job.class));
    }

    @Test
    public void getJobInforById_validId_returnsJobInfo() {
        // Arrange

        Job_Category job_category = new Job_Category();
        job_category.setId(1);
        job_category.setName("Software Engineer");
        job_category.setDeleted(false);

        Branch branch = new Branch();
        branch.setId(1);
        branch.setName("Head Office");
        branch.setAddress("123 Main St, City, Country");

        Job job = new Job();
        job.setId(1);
        job.setName("Software Engineer");
        job.setJob_Type("Full Time");
        job.setDescription("We are looking for a software engineer");
        job.setApply_Before(Date.valueOf("2022-12-31"));
        job.setAddress("123 Main St, City, Country");
        job.setCareer_Level("Junior");
        job.setExperience(2);
        job.setOffer_Salary("$2000-$3000");
        job.setQualification("Bachelor Degree");
        job.setBranch(branch);
        job.setJob_category(job_category);

        Optional<Job> jobOptional = Optional.of(job);
        when(jobRepo.findById(job.getId())).thenReturn(jobOptional);
        Optional<Branch> optionalBranch = Optional.of(branch);
        when(branchRepo.findById(job.getBranch().getId())).thenReturn(optionalBranch);

        // Act
        ResponseEntity<?> response = jobService.getJobInforById(job.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JobDTO jobDTO = (JobDTO) response.getBody();
        assertEquals(job.getName(), jobDTO.getName());
        assertEquals(job.getBranch().getId(), jobDTO.getBranch_Id());
        assertEquals(job.getJob_category().getId(), jobDTO.getCategory_Id());
        assertEquals(job.getCareer_Level(), jobDTO.getCareer_Level());
        assertEquals(job.getExperience(), jobDTO.getExperience());
        assertEquals(job.getOffer_Salary(), jobDTO.getOffer_Salary());
        assertEquals(job.getQualification(), jobDTO.getQualification());
        assertEquals(job.getJob_Type(), jobDTO.getJob_Type());
        assertEquals(job.getDescription(), jobDTO.getDescription());
        assertEquals(job.getApply_Before(), jobDTO.getApply_Before());
        assertEquals(job.getAddress(), jobDTO.getAddress());
        assertEquals(job.getCreated_At(), jobDTO.getCreate_At());
        assertEquals(job.getUpdated_At(), jobDTO.getUpdate_At());
    }

    @Test
    public void getJobInforById_invalidId_returnsError() {
        // Arrange
        int id = 1;
        Optional<Job> jobOptional = Optional.empty();
        when(jobRepo.findById(id)).thenReturn(jobOptional);

        // Act
        ResponseEntity<?> response = jobService.getJobInforById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> error = (Map<String, Object>) response.getBody();
        assertEquals("Job not found", error.get("error"));
    }

    @Test
    public void deleteJob_validId_returnsSuccess() {
        // Arrange
        Job job = new Job();
        job.setId(1);
        job.setName("Software Engineer");
        job.setJob_Type("Full Time");
        job.setDescription("We are looking for a software engineer");
        job.setApply_Before(Date.valueOf("2022-12-31"));
        job.setAddress("123 Main St, City, Country");
        job.setCareer_Level("Junior");
        job.setExperience(2);
        job.setOffer_Salary("$2000-$3000");
        job.setQualification("Bachelor Degree");
        Optional<Job> jobOptional = Optional.of(job);
        when(jobRepo.findById(job.getId())).thenReturn(jobOptional);

        // Act
        ResponseEntity<?> response = jobService.deleteJob(job.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> success = (Map<String, Object>) response.getBody();
        assertEquals("Xóa thành công !!!", success.get("success"));
    }

    @Test
    public void deleteJob_invalidId_returnsError() {
        // Arrange
        int id = 1;
        Optional<Job> jobOptional = Optional.empty();
        when(jobRepo.findById(id)).thenReturn(jobOptional);

        // Act
        ResponseEntity<?> response = jobService.deleteJob(id);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> error = (Map<String, Object>) response.getBody();
        assertEquals("Not exist !!!", error.get("error"));
    }
}