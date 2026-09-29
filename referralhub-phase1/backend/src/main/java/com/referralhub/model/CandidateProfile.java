package com.referralhub.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CandidateProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private User candidate;

    @Column(length = 3000)
    private String skills;

    @Column(length = 2000)
    private String experience;

    @Column(length = 4000)
    private String projects;

    @Column(length = 1000)
    private String preferredRoles;

    @Column(length = 1000)
    private String preferredLocations;

    private Integer graduationYear;

    @Column(length = 50)
    private String phone;

    @Column(length = 1000)
    private String currentLocation;
}
