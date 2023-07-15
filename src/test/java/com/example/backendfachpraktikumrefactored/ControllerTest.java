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
        // Arrange
        String id = UUID.randomUUID().toString();
        Document newDocument = new Document(); // Assume there's a no-args constructor
        newDocument.setGuid(UUID.fromString(id));
        // Configure the mock to return the existing document when `findDocumentAsJSON` is called
        when(documentService.findDocumentAsJSON(UUID.fromString(id))).thenReturn(Optional.of(new Document()));
        // Act
        ResponseEntity<?> result = documentController.updateDocument(id, newDocument);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        // Verify that the mock's `saveDocument` method was called once with the `newDocument`
        verify(documentService, times(1)).saveDocument(newDocument);
    }

    @Test
    public void testUpdateDocumentWhenDocumentDoesNotExist() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Document newDocument = new Document(); // Assume there's a no-args constructor
        newDocument.setGuid(UUID.fromString(id));
        // Configure the mock to return an empty Optional when `findDocumentAsJSON` is called
        when(documentService.findDocumentAsJSON(UUID.fromString(id))).thenReturn(Optional.empty());
        // Act
        ResponseEntity<?> result = documentController.updateDocument(id, newDocument);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        // Verify that the mock's `saveDocument` method was not called
        verify(documentService, never()).saveDocument(newDocument);
    }

    @Test
    public void testUpdateDocumentWhenExceptionOccurs() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Document newDocument = new Document(); // Assume there's a no-args constructor
        newDocument.setGuid(UUID.fromString(id));
        // Configure the mock to throw an exception when `findDocumentAsJSON` is called
        when(documentService.findDocumentAsJSON(UUID.fromString(id))).thenThrow(new RuntimeException());
        // Act
        ResponseEntity<?> result = documentController.updateDocument(id, newDocument);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        // Verify that the mock's `saveDocument` method was not called
        verify(documentService, never()).saveDocument(newDocument);
    }
}
