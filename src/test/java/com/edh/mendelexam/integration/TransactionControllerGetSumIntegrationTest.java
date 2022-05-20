package com.edh.mendelexam.integration;

import com.edh.mendelexam.MendelExamApplicationTests;
import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.harness.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TransactionControllerGetSumIntegrationTest extends MendelExamApplicationTests {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    Map<Long, TransactionNode> transactions;

    @BeforeEach
    void setUp() {
        transactions.clear();
    }

    @Test
    public void getSum_withTransactionWithoutChilds_mustReturnAmountOfTransaction() throws IOException {
        transactions.put(1L, new TransactionNode(1L, 200d, null, "cash"));
        ResponseEntity<String> result = testRestTemplate.getForEntity("/transactions/sum/1", String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(result.getBody()).isEqualTo(FileReader.read("./src/test/resources/harness/responses/getSum_totalAmount200.json"));
    }

    @Test
    public void getSum_withInvalidId_mustReturn404HttpStatus() {
        ResponseEntity<String> result = testRestTemplate.getForEntity("/transactions/sum/1", String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getSum_withTransactionWithChilds_mustReturnSumOfAmounts() throws IOException {
        transactions.put(1L, new TransactionNode(1L, 200d, null, "cash"));
        transactions.put(2L, new TransactionNode(2L, 200d, 1L, "cash"));
        transactions.put(3L, new TransactionNode(3L, 200d, 1L, "cash"));
        transactions.put(4L, new TransactionNode(4L, 200d, 3L, "cash"));
        transactions.get(1L).addChild(transactions.get(2L));
        transactions.get(1L).addChild(transactions.get(3L));
        transactions.get(3L).addChild(transactions.get(4L));
        ResponseEntity<String> result = testRestTemplate.getForEntity("/transactions/sum/1", String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(result.getBody()).isEqualTo(FileReader.read("./src/test/resources/harness/responses/getSum_totalAmount800.json"));
    }

}
