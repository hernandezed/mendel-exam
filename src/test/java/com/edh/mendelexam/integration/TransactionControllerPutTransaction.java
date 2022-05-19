package com.edh.mendelexam.integration;

import com.edh.mendelexam.MendelExamApplicationTests;
import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.harness.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionControllerPutTransaction extends MendelExamApplicationTests {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    Map<Long, TransactionNode> transactions;

    @BeforeEach
    void setUp() {
        transactions.clear();
    }

    @Test
    void putTransaction_withValidTransactionAndTransactionNotExists_mustReturn201HttpStatusWithResponseBody() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/validTransaction.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThatJson(response.getBody()).isEqualTo(FileReader.read("./src/test/resources/harness/responses/putTransaction_withValidTransaction_mustReturnCreatedTransaction.json"));
    }

    @Test
    void putTransaction_withPreexistedTransaction_mustReturn204HttpStatusWithoutBody() throws IOException {
        transactions.put(1L, new TransactionNode(1L, 20d, null, "transfer"));
        String requestBody = FileReader.read("./src/test/resources/harness/requests/validTransaction.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThatJson(response.getBody()).isEqualTo(null);
    }

    @Test
    void putTransaction_withZeroAmountTransaction_mustReturn400HttpStatus() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/zeroAmountTransaction.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
