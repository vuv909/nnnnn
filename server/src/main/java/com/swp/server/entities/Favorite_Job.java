package com.swp.server.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Favorite_Job")
public class Favorite_Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int Id;
    @ManyToOne
    @JoinColumn(name = "Job_Id", referencedColumnName = "Id")
    private Job job;
    @ManyToOne
    @JoinColumn(name = "Account_Id", referencedColumnName = "Id")
    private Account account;

    public Favorite_Job() {
    }

    public Favorite_Job(int id, Job job, Account account) {
        Id = id;
        this.job = job;
        this.account = account;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
