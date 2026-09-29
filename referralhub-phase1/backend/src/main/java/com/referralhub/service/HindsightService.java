package com.referralhub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HindsightService {

    private static final Logger log =
            LoggerFactory.getLogger(HindsightService.class);

    private final RestClient client;

    @Value("${hindsight.enabled:false}")
    private boolean enabled;

    @Value("${hindsight.base-url:http://localhost:8888}")
    private String baseUrl;

    @Value("${hindsight.api-key:}")
    private String apiKey;

    @Value("${hindsight.bank-prefix:referralhub}")
    private String bankPrefix;

    public HindsightService(RestClient.Builder builder) {
        this.client = builder.build();
    }

    public void retain(String scope, String content, String context) {

        if (!enabled) {
            log.warn("Hindsight is DISABLED. Memory was not retained.");
            return;
        }

        String bank = bankId(scope);

        log.info("Hindsight retain started. Bank: {}", bank);

        try {

            // Make sure the memory bank exists
            ensureBank(bank);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("content", content);
            item.put("context", context);

            Map<String, Object> body =
                    Map.of("items", List.of(item));

            client.post()
                    .uri(
                            baseUrl
                                    + "/v1/default/banks/{bankId}/memories",
                            bank
                    )
                    .headers(this::auth)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            log.info(
                    "Hindsight memory RETAINED successfully. Bank: {}",
                    bank
            );

        } catch (Exception e) {

            log.error(
                    "Hindsight RETAIN failed. Bank: {} | Error: {}",
                    bank,
                    e.getMessage(),
                    e
            );
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> recall(String scope, String query) {

        if (!enabled) {
            log.warn("Hindsight is DISABLED. Recall skipped.");
            return List.of();
        }

        String bank = bankId(scope);

        log.info(
                "Hindsight recall started. Bank: {} | Query: {}",
                bank,
                query
        );

        try {

            // Make sure the memory bank exists
            ensureBank(bank);

            Map<String, Object> response =
                    client.post()
                            .uri(
                                    baseUrl
                                            + "/v1/default/banks/{bankId}/memories/recall",
                                    bank
                            )
                            .headers(this::auth)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of("query", query))
                            .retrieve()
                            .body(Map.class);

            if (response == null) {

                log.warn(
                        "Hindsight recall returned NULL response. Bank: {}",
                        bank
                );

                return List.of();
            }

            log.info(
                    "Hindsight recall response received. Bank: {} | Response: {}",
                    bank,
                    response
            );

            Object raw = response.get("results");

            if (!(raw instanceof List<?> results)) {

                log.warn(
                        "Hindsight response does not contain a 'results' list. Bank: {}",
                        bank
                );

                return List.of();
            }

            List<String> memories = results.stream()
                    .filter(Map.class::isInstance)
                    .map(item ->
                            ((Map<String, Object>) item).get("text")
                    )
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .limit(6)
                    .collect(Collectors.toList());

            log.info(
                    "Hindsight recall completed. Bank: {} | Memories found: {}",
                    bank,
                    memories.size()
            );

            return memories;

        } catch (Exception e) {

            log.error(
                    "Hindsight RECALL failed. Bank: {} | Error: {}",
                    bank,
                    e.getMessage(),
                    e
            );

            return List.of();
        }
    }

    private void ensureBank(String bank) {

        log.info(
                "Ensuring Hindsight bank exists: {}",
                bank
        );

        try {

            client.put()
                    .uri(
                            baseUrl
                                    + "/v1/default/banks/{bankId}",
                            bank
                    )
                    .headers(this::auth)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Map.of(
                                    "name",
                                    "ReferralHub " + bank
                            )
                    )
                    .retrieve()
                    .toBodilessEntity();

            log.info(
                    "Hindsight bank ready: {}",
                    bank
            );

        } catch (Exception e) {

            log.error(
                    "Hindsight BANK creation/check failed. Bank: {} | Error: {}",
                    bank,
                    e.getMessage(),
                    e
            );

            // Re-throw so retain/recall also knows the bank operation failed
            throw e;
        }
    }

    private void auth(HttpHeaders headers) {

        if (apiKey != null && !apiKey.isBlank()) {

            headers.setBearerAuth(apiKey);

        } else {

            log.warn(
                    "Hindsight API key is EMPTY."
            );
        }
    }

    private String bankId(String scope) {

        return bankPrefix
                + "-"
                + scope
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9_-]", "-");
    }
}