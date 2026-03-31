package com.example.tableviewer.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_demo")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String email;

    @Column(length = 500)
    private String address;

    @Column(length = 200)
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "joindate")
    private LocalDateTime joinDate;

    public User() {}

    public User(String name, String email, String address, String password, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.password = password;
        this.birthday = birthday;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
}
