package com.swp.server.dto;

public class UpdateJobApplyDTO {
    private int id;
    private String status;

    public UpdateJobApplyDTO() {
    }

    public UpdateJobApplyDTO(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
