package com.example.backendfachpraktikumrefactored;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.backendfachpraktikumrefactored.Model.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;
@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    DocumentService documentService;

    @InjectMocks
    Controller documentController;

    @Test
    public void getDocumentSuccessTest() {
        Document document = new Document();
        when(documentService.findDocumentAsJSON(any(UUID.class))).thenReturn(Optional.of(document));

        ResponseEntity<?> response = documentController.getDocument("123e4567-e89b-12d3-a456-556642440000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(document, response.getBody());
    }

    @Test
    public void getDocumentNotFoundTest() {
        when(documentService.findDocumentAsJSON(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = documentController.getDocument("123e4567-e89b-12d3-a456-556642440000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Document not found", response.getBody());
    }

    @Test
    public void testUpdateDocumentWhenDocumentExists() {

        String id = UUID.randomUUID().toString();
        Document newDocument = new Document();
        newDocument.setGuid(UUID.fromString(id));
        when(documentService.findDocumentAsJSON(UUID.fromString(id))).thenReturn(Optional.of(new Document()));
        ResponseEntity<?> result = documentController.updateDocument(id, newDocument);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(documentService, times(1)).saveDocument(newDocument);
    }

    @Test
    public void testUpdateDocumentWhenDocumentDoesNotExist() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Document newDocument = new Document();
        newDocument.setGuid(UUID.fromString(id));
        when(documentService.findDocumentAsJSON(UUID.fromString(id))).thenReturn(Optional.empty());
        ResponseEntity<?> result = documentController.updateDocument(id, newDocument);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(documentService, never()).saveDocument(newDocument);
    }

    @Test
    public void testUpdateDocumentWhenExceptionOccurs() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Document newDocument = new Document();
        newDocument.setGuid(UUID.fromString(id));
        when(documentService.findDocumentAsJSON(UUID.fromString(id))).thenThrow(new RuntimeException());
        ResponseEntity<?> result = documentController.updateDocument(id, newDocument);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(documentService, never()).saveDocument(newDocument);
    }
}
