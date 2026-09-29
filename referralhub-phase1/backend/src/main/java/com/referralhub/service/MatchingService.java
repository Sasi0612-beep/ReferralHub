package com.referralhub.service;

import com.referralhub.dto.MatchResult;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    public MatchResult calculate(String candidateSkills, String requiredSkills, String preferredSkills) {
        Set<String> candidate = normalize(candidateSkills);
        List<String> required = orderedSkills(requiredSkills);
        List<String> preferred = orderedSkills(preferredSkills);

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : required) {
            if (candidate.contains(skill)) matched.add(skill);
            else missing.add(skill);
        }

        for (String skill : preferred) {
            if (candidate.contains(skill) && !matched.contains(skill)) matched.add(skill);
        }

        int requiredScore = required.isEmpty() ? 100 :
                (int) Math.round(matchedRequired(candidate, required) * 70.0 / required.size());

        int preferredScore = preferred.isEmpty() ? 30 :
                (int) Math.round(matchedPreferred(candidate, preferred) * 30.0 / preferred.size());

        int percentage = Math.min(100, requiredScore + preferredScore);
        return new MatchResult(percentage, matched, missing);
    }

    private long matchedRequired(Set<String> candidate, List<String> required) {
        return required.stream().filter(candidate::contains).count();
    }

    private long matchedPreferred(Set<String> candidate, List<String> preferred) {
        return preferred.stream().filter(candidate::contains).count();
    }

    private List<String> orderedSkills(String value) {
        if (value == null || value.isBlank()) return List.of();
        return Arrays.stream(value.split("[,;|]"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }

    private Set<String> normalize(String value) {
        return new HashSet<>(orderedSkills(value));
    }
}
