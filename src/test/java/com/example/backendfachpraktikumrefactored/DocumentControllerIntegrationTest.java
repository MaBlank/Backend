package com.example.backendfachpraktikumrefactored;

import com.example.backendfachpraktikumrefactored.Model.Annotation;
import com.example.backendfachpraktikumrefactored.Model.Annotations;
import com.example.backendfachpraktikumrefactored.Model.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DocumentControllerIntegrationTest {
    @Autowired
    private AnnotationRepository annotationRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @BeforeEach
    public void setup() {
        Annotation annotation = new Annotation(1, 5, "Label", "Color");
        Annotations annotations = new Annotations(List.of(annotation));
        Document document = new Document(
                UUID.fromString("28aa4362-7229-44bd-80fa-6d0495c97100"),
                "Name",
                "Text",
                annotations);
        annotationRepository.save(document);
    }
    @AfterEach
    public void tearDown() {
        annotationRepository.deleteByGuid(
                UUID.fromString("28aa4362-7229-44bd-80fa-6d0495c97100"));
    }
    @Test
    public void testUpdateDocumentIntegration() {
        String url = "/api/update/28aa4362-7229-44bd-80fa-6d0495c97100";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String requestJson ="" +
                "{\"guid\":\"28aa4362-7229-44bd-80fa-6d0495c97100\", " +
                "\"name\":\"Document\", " +
                "\"text\":\"I love apples!\", " +
                "\"annotations\":{\"annotations\":[{\"start\":7, \"end\":23, " +
                "\"label\":\"FRUIT\", " +
                "\"color\":\"greener\"}]}}";
        HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
