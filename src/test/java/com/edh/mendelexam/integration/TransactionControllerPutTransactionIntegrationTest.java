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
import java.util.List;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionControllerPutTransactionIntegrationTest extends MendelExamApplicationTests {

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
        assertThatJson(response.getBody()).isEqualTo(FileReader.read("./src/test/resources/harness/responses/createTransactionStatusOk.json"));
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

    @Test
    void putTransaction_withAmountLessThanZero_mustReturn400HttpStatus() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/lessThanZeroAmountTransaction.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void putTransaction_withNullType_mustReturn400HttpStatus() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/nullTypeTransaction.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void putTransaction_withEmptyType_mustReturn400HttpStatus() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/emptyTypeTransaction.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void putTransaction_withValidParentType_mustCreateTransactionAndAssignAsChild() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/validTransactionWithParent.json");
        String url = "/transactions/1";
        transactions.put(5L, new TransactionNode(5L, 200d, null, "transfer"));
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);

        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(transactions.get(5L).getChilds()).extracting(TransactionNode::getId).isEqualTo(List.of(1L));
        assertThatJson(response.getBody()).isEqualTo(FileReader.read("./src/test/resources/harness/responses/createTransactionStatusOk.json"));
    }

    @Test
    void putTransaction_withValidTransactionWithInvalidParent_mustReturn400HttpStatus() throws IOException {
        String requestBody = FileReader.read("./src/test/resources/harness/requests/validTransactionWithParent.json");
        String url = "/transactions/1";
        RequestEntity<String> request = RequestEntity.put(url).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(requestBody);

        ResponseEntity<String> response = testRestTemplate.exchange(request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThatJson(response.getBody()).inPath("$.status").isEqualTo("error");
    }
}
