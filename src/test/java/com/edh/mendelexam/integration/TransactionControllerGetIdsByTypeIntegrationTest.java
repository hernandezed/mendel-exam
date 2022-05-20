package com.edh.mendelexam.integration;

import com.edh.mendelexam.MendelExamApplicationTests;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TransactionControllerGetIdsByTypeIntegrationTest extends MendelExamApplicationTests {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    Map<String, Set<Long>> types;

    @BeforeEach
    void setUp() {
        types.clear();
    }

    @Test
    public void getIdsByType_whenExistsCategoryAndHasIds_mustReturnNotEmptySet() {
        types.put("cash", Sets.newHashSet(1L, 2L));
        ResponseEntity<String> result = testRestTemplate.getForEntity("/transactions/types/cash", String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(result.getBody()).isEqualTo("[1,2]");
    }

    @Test
    public void getIdsByType_whenNoExistsCategoryAndHasIds_mustReturnEmptySet() {
        ResponseEntity<String> result = testRestTemplate.getForEntity("/transactions/types/transfer", String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
