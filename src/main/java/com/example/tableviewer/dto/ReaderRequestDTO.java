package com.example.tableviewer.dto;

import java.time.LocalDate;

public class ReaderRequestDTO {

    private Long id;
    private String name;
    private String gender;
    private LocalDate birthday;
    private LocalDate registeredDate;
    private Boolean activeFlag;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public LocalDate getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(LocalDate registeredDate) { this.registeredDate = registeredDate; }

    public Boolean getActiveFlag() { return activeFlag; }
    public void setActiveFlag(Boolean activeFlag) { this.activeFlag = activeFlag; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
