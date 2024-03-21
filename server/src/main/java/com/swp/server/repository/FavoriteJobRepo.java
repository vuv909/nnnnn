package com.swp.server.repository;

import com.swp.server.dto.AccountDTO;
import com.swp.server.entities.Account;
import com.swp.server.entities.Favorite_Job;
import com.swp.server.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteJobRepo extends JpaRepository<Favorite_Job, Integer> {
    Optional<Favorite_Job> findFirstByAccount_Id(int accountId);
    List<Favorite_Job> findByAccount_Id(int accountId);
    List<Favorite_Job> findByJob_Id(int jobId);
    Optional<Favorite_Job> findFirstByJob_Id(int jobId);
    Optional<Favorite_Job> findFirstById(int favoriteId);

    List<Favorite_Job> findByAccountAndJob(Account account, Job job);
}