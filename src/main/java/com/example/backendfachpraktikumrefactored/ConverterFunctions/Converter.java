package com.example.backendfachpraktikumrefactored.ConverterFunctions;

import com.example.backendfachpraktikumrefactored.Model.Annotation;
import com.example.backendfachpraktikumrefactored.Model.Annotations;
import com.example.backendfachpraktikumrefactored.Model.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Converter {
    public static Document convertXmlToDocument(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        Document document = (Document) unmarshaller.unmarshal(reader);
        return document;
    }
    public static Document convertDocxToDocument(String filePath) {
        List<String> sentencesList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph para : paragraphs) {
                sentencesList.add(para.getText());
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Document(UUID.randomUUID(), "Document Name", String.join(" ", sentencesList), new Annotations(new ArrayList<>()));
    }
    public static String convertDocumentToXml(Document document) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(document, sw);
        return sw.toString();
    }
    public static Document convertTxtToDocuments(String txt) {
        return new Document(UUID.randomUUID(), "Document Name",txt, new Annotations(new ArrayList<>()));
    }

    public static String convertDocumentToCoNLL2003(Document document) {
        StringBuilder conll2003 = new StringBuilder();
        String[] tokens = document.getText().split("(?<=\\w)(?=[.,!?])|\\s+");
        List<Annotation> annotations = document.getAnnotations().getAnnotations();
        int currentAnnotationIndex = 0;

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[.,!?]")) {
                conll2003.append(tokens[i]).append("\n");
                if (i == tokens.length - 1) {
                    conll2003.append("\n");
                }
            } else {
                conll2003.append(tokens[i]);
                if (currentAnnotationIndex < annotations.size() &&
                        i == getWordIndex(tokens, annotations.get(currentAnnotationIndex).getStart())) {
                    conll2003.append(" ").append(annotations.get(currentAnnotationIndex).getLabel());
                    currentAnnotationIndex++;
                } else {
                    conll2003.append(" O");
                }
                conll2003.append("\n");
            }
        }
        return conll2003.toString().trim();
    }
    private static int getWordIndex(String[] tokens, int charIndex) {
        int sum = 0;
        for (int i = 0; i < tokens.length; i++) {
            sum += tokens[i].length() + 1;
            if (charIndex < sum) {
                return i;
            }
        }
        return -1;
    }
}
