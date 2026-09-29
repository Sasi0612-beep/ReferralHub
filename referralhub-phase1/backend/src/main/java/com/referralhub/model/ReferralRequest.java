package com.referralhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referral_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReferralRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Job job;

    @ManyToOne(optional = false)
    private User candidate;

    @ManyToOne(optional = false)
    private User employee;

    private int matchPercentage;

    @Column(length = 2000)
    private String matchedSkills;

    @Column(length = 2000)
    private String missingSkills;

    @Enumerated(EnumType.STRING)
    private ReferralStatus status;

    @Column(length = 4000)
    private String aiAnalysis;

    private LocalDateTime createdAt;

    public enum ReferralStatus {
        PENDING, REFERRED, DECLINED
    }
}
