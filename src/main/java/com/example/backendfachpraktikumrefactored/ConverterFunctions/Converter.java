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

        // Set the Marshaller properties to generate pretty-formatted XML
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format the XML output
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // To specify the encoding
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); // To remove the XML declaration at the start of the XML

        StringWriter sw = new StringWriter();
        marshaller.marshal(document, sw);
        return sw.toString();
    }
    public static Document convertTxtToDocuments(String txt) {
        return new Document(UUID.randomUUID(), "Document Name",txt, new Annotations(new ArrayList<>()));
    }
    public static Document convertCoNLL2003ToDocument(String conll2003) {
        String[] lines = conll2003.split("\n");
        StringBuilder sentence = new StringBuilder();
        List<Annotation> annotations = new ArrayList<>();
        int currentIndex = 0;

        for (String line : lines) {
            if (line.trim().isEmpty()) {  // new sentence
                continue;
            }
            String[] parts = line.split(" ");
            if (parts.length < 2) {
                sentence.append(parts[0]);  // Add punctuation to the sentence.
                currentIndex += parts[0].length();
                continue;  // skip lines without a tag
            }
            String token = parts[0];
            String tag = parts[1];

            if (sentence.length() > 0) { // add a space if not the start of the sentence
                sentence.append(" ");
                currentIndex += 1;
            }

            sentence.append(token);
            if (!"O".equals(tag)) {
                annotations.add(new Annotation(currentIndex, currentIndex + token.length(),"name", tag));
            }
            currentIndex += token.length();
        }
        return new Document(UUID.randomUUID(), "Document Name",sentence.toString().trim(), new Annotations(annotations));
    }
    public static String convertDocumentToCoNLL2003(Document document) {
        StringBuilder conll2003 = new StringBuilder();
        String[] tokens = document.getText().split("(?<=\\w)(?=[.,!?])|\\s+");
        List<Annotation> annotations = document.getAnnotations().getAnnotations();
        int currentAnnotationIndex = 0;

        for (int i = 0; i < tokens.length; i++) {
            // If token is a punctuation mark, skip label
            if (tokens[i].matches("[.,!?]")) {
                conll2003.append(tokens[i]).append("\n");
                if (i == tokens.length - 1) { // If it's the last token in the sentence
                    conll2003.append("\n"); // Add an additional newline character
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

        // Return the trimmed string to remove leading and trailing whitespace
        return conll2003.toString().trim();
    }
    private static int getWordIndex(String[] tokens, int charIndex) {
        int sum = 0;
        for (int i = 0; i < tokens.length; i++) {
            sum += tokens[i].length() + 1;  // +1 for space or punctuation
            if (charIndex < sum) {
                return i;
            }
        }
        return -1;
    }
}
