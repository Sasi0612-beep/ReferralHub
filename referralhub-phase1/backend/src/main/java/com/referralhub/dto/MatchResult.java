package com.referralhub.dto;

import java.util.List;

public record MatchResult(
        int percentage,
        List<String> matchedSkills,
        List<String> missingSkills
) {}
