package com.example.backendfachpraktikumrefactored;
import com.example.backendfachpraktikumrefactored.Model.Document;
import com.example.backendfachpraktikumrefactored.ConverterFunctions.Converter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.JAXBException;

@Service
@AllArgsConstructor
public class DocumentService {
    private final AnnotationRepository annotationRepository;
    public Document uploadDocx(String name, MultipartFile file) throws IOException {
        String rootDir = System.getProperty("user.dir");
        String dirPath = rootDir + File.separator + "temp" + File.separator;
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
        String filePath = dirPath + file.getOriginalFilename();
        File docxFile = new File(filePath);
        file.transferTo(docxFile);
        Document documents = null;
        try {
            documents = Converter.convertDocxToDocument(filePath);
            documents.setName(name);
            Document savedDocuments = annotationRepository.save(documents);
            return savedDocuments;
        } finally {
            // delete temporary file
            if (docxFile != null) {
                docxFile.delete();
            }
        }
    }

    public Document uploadXml(String name, String xml) throws JAXBException {
        Document documents = Converter.convertXmlToDocument(xml);
        documents.setName(name);
        Optional<Document> document = annotationRepository.findByGuid(documents.getGuid());
        if (document.isPresent()) {
            annotationRepository.deleteByGuid(documents.getGuid());
        }
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public void deleteAllDocuments() {
        annotationRepository.deleteAll();
    }

    public Document uploadTxt(String name, String txt) {
        Document documents = Converter.convertTxtToDocuments(txt);
        documents.setName(name);
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public Document uploadJson(Document documents) {
        Optional<Document> document = annotationRepository.findByGuid(documents.getGuid());
        if (document.isPresent()) {
            annotationRepository.deleteByGuid(documents.getGuid());
        }
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public void deleteDocument(UUID guid) {
        annotationRepository.deleteByGuid(guid);
    }
    public Optional<Document> findDocumentAsJSON(UUID guid) {
        return annotationRepository.findByGuid(guid);
    }
    public String findDocumentAsXml(UUID guid) throws JAXBException {
        Optional<Document> document = annotationRepository.findByGuid(guid);
        if (document.isPresent()) {
            return Converter.convertDocumentToXml(document.get());
        }
        return null;
    }
    public void saveDocument(Document document) {
        annotationRepository.save(document);
    }
    public List<Document> getAllDocuments() {
        return annotationRepository.findAll();
    }
}
