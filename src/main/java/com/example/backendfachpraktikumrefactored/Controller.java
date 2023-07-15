package com.example.backendfachpraktikumrefactored;
import com.example.backendfachpraktikumrefactored.Helper.TextObject;
import com.example.backendfachpraktikumrefactored.Model.Document;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class Controller {
    private final DocumentService documentService;
    @PostMapping("/uploadDocx")
    public ResponseEntity uploadDocx(String name, @RequestParam("file") MultipartFile file) {
        try {
            Document savedDocuments = documentService.uploadDocx(name,file);
            return new ResponseEntity<>(savedDocuments, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/uploadTxt")
    public ResponseEntity<String> uploadTxt(@RequestParam("name") String name, @RequestBody String text) {
        String result = String.valueOf(documentService.uploadTxt(name, text));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/uploadXml")
    public ResponseEntity uploadXml(String name, @RequestBody String xml) {
        try {
            Document savedDocuments = documentService.uploadXml(name, xml);
            return new ResponseEntity<>(savedDocuments, HttpStatus.OK);
        } catch (JAXBException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/uploadCoNLL2003")
    public ResponseEntity uploadCoNLL2003(String name, @RequestBody String conll2003) {
        try {
            Document savedDocuments = documentService.uploadCoNLL2003(name, conll2003);
            return new ResponseEntity<>(savedDocuments, HttpStatus.OK);
        } catch (Exception e) {
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
    @PostMapping("/uploadJson")
    public ResponseEntity uploadJson(@RequestBody Document documents) {
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
    public ResponseEntity<?> getXml(@PathVariable String id) {
        try {
            String xml = documentService.findDocumentAsXml(UUID.fromString(id));
            if (xml != null) {
                return new ResponseEntity<>(xml, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/conll2003/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getCoNLL2003(@PathVariable String id) {
        try {
            String conll2003 = documentService.findDocumentAsCoNLL2003(UUID.fromString(id));
            if (conll2003 != null) {
                return new ResponseEntity<>(conll2003, HttpStatus.OK);
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
}
