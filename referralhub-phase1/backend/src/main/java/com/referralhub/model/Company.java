package com.referralhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String website;
    private String industry;

    @Column(length = 2000)
    private String description;

    private LocalDateTime createdAt;
}
