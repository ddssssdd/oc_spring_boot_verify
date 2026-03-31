package com.example.tableviewer.dto;

import com.example.tableviewer.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String address;
    private LocalDate birthday;
    private LocalDateTime joinDate;

    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setAddress(user.getAddress());
        r.setBirthday(user.getBirthday());
        r.setJoinDate(user.getJoinDate());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
}
