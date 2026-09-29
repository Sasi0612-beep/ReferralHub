package com.referralhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String jobCode;

    @Column(nullable = false)
    private String title;

    private String location;

    @Column(length = 4000)
    private String description;

    @Column(length = 2000)
    private String requiredSkills;

    @Column(length = 2000)
    private String preferredSkills;

    private String experience;

    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    private Company company;

    @ManyToOne(optional = false)
    private User employee;

    private boolean active;
}
