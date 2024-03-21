package com.swp.server.dto;

public class SearchAccountDTO {
    private String txtSearch;
    private int roleId;

    public String getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(String txtSearch) {
        this.txtSearch = txtSearch;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public SearchAccountDTO(String txtSearch, int roleId) {
        this.txtSearch = txtSearch;
        this.roleId = roleId;
    }
}
