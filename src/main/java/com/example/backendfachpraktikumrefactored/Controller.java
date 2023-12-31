package com.example.backendfachpraktikumrefactored;
import com.example.backendfachpraktikumrefactored.Model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class Controller {
    @Autowired
    private ResourceLoader resourceLoader;
    private final DocumentService documentService;
    @PostMapping("/uploadDocx")
    public ResponseEntity<?> uploadDocx(String name, @RequestParam("file") MultipartFile file) {
        try {
            Document savedDocuments = documentService.uploadDocx(name, file);
            return new ResponseEntity<>(savedDocuments, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/uploadTxt")
    public ResponseEntity<String> uploadTxt(@RequestParam("name") String name, @RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        String text = "";
        try {
            Map<String, String> map = mapper.readValue(json, Map.class);
            text = map.get("txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = String.valueOf(documentService.uploadTxt(name, text));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/uploadXml")
    public ResponseEntity<?> uploadXml(String name, @RequestBody String xml) {
        try {
            Document savedDocuments = documentService.uploadXml(name, xml);
            return new ResponseEntity<>(savedDocuments, HttpStatus.OK);
        } catch (JAXBException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAllDocuments")
    public ResponseEntity<Void> deleteAllDocuments() {
        try {
            documentService.deleteAllDocuments();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deleteDocument/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        try {
            documentService.deleteDocument(UUID.fromString(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Te
    @PostMapping("/uploadJson")
    public ResponseEntity<?> uploadJson(@RequestBody Document documents) {
        try {
            Document savedDocuments = documentService.uploadJson(documents);
            return new ResponseEntity<>(savedDocuments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<?> getDocument(@PathVariable String id) {
        try {
            Optional<Document> document = documentService.findDocumentAsJSON(UUID.fromString(id));
            if (document.isPresent()) {
                return new ResponseEntity<>(document.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Resource> getXml(@PathVariable String id) {
        try {
            String xml = documentService.findDocumentAsXml(UUID.fromString(id));
            if (xml != null) {
                ByteArrayResource resource = new ByteArrayResource(xml.getBytes(StandardCharsets.UTF_8));
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xml");
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDocument(@PathVariable String id, @RequestBody Document newDocument) {
        try {
            Optional<Document> document = documentService.findDocumentAsJSON(UUID.fromString(id));
            if (document.isPresent()) {
                documentService.deleteDocument(UUID.fromString(id));
                newDocument.setGuid(UUID.fromString(id));
                documentService.saveDocument(newDocument);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/projects")
    public ResponseEntity<List<Document>> getAllDocuments() {
        try {
            List<Document> allDocumentIds = documentService.getAllDocuments();
            if (!allDocumentIds.isEmpty()) {
                return new ResponseEntity<>(allDocumentIds, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projectsPerformance")
    // Tests the Performance
    public ResponseEntity<String> getAllDocuments2() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("LangerText.txt");
            if (is == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (!content.isEmpty()) {
                return new ResponseEntity<>(content, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
