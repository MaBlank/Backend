package com.example.backendfachpraktikumrefactored;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.backendfachpraktikumrefactored.Model.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    AnnotationRepository annotationRepository;
    @InjectMocks
    DocumentService documentService;
    @Test
    public void findDocumentAsJSONTest() {
        Document document = new Document();
        when(annotationRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(document));
        Optional<Document> result = documentService.findDocumentAsJSON(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        assertTrue(result.isPresent());
        assertEquals(document, result.get());
    }

    @Test
    public void findDocumentAsJSONNotFoundTest() {
        when(annotationRepository.findByGuid(any(UUID.class))).thenReturn(Optional.empty());
        Optional<Document> result = documentService.findDocumentAsJSON(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        assertFalse(result.isPresent());
    }
}
