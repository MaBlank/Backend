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
     public Document uploadDocx(MultipartFile file) throws IOException {
        String filePath = "path/to/temp/dir/" + file.getOriginalFilename();
        File docxFile = new File(filePath);
        file.transferTo(docxFile);
        Document documents = Converter.convertDocxToDocument(filePath);
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public Document uploadXml(String xml) throws JAXBException {
        Document documents = Converter.convertXmlToDocument(xml);
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public Document uploadCoNLL2003(String conll2003) {
        Document documents = Converter.convertCoNLL2003ToDocument(conll2003);
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public Document uploadTxt(String txt) {
        Document documents = Converter.convertTxtToDocuments(txt);
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
    }
    public Document uploadJson(Document documents) {
        Document savedDocuments = annotationRepository.save(documents);
        return savedDocuments;
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
    public String findDocumentAsCoNLL2003(UUID guid) {
        Optional<Document> document = annotationRepository.findByGuid(guid);
        if (document.isPresent()) {
            return Converter.convertDocumentToCoNLL2003(document.get());
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
