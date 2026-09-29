package com.referralhub.dto;

import java.util.List;

public record ReferralAnalysis(
        int matchPercentage,
        List<String> matchedSkills,
        List<String> missingSkills,
        List<String> memories,
        String explanation
) {}
