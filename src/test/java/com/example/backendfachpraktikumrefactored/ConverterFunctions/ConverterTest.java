package com.example.backendfachpraktikumrefactored.ConverterFunctions;
import com.example.backendfachpraktikumrefactored.Model.Annotation;
import com.example.backendfachpraktikumrefactored.Model.Annotations;
import com.example.backendfachpraktikumrefactored.Model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static com.example.backendfachpraktikumrefactored.ConverterFunctions.Converter.convertTxtToDocuments;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    private Converter converterFunctions;

    @BeforeEach
    void setup() {
        converterFunctions = new Converter();
    }

    @Test
    void testConvertXmlToDocument() throws JAXBException {
    String xml = "<Document guid=\"28aa4362-7229-44bd-80fa-6d0495c97100\">\n" +
                "    <name>Document Name</name>\n" +
                "    <text>I love apples!</text>\n" +
                "    <annotations>\n" +
                "        <annotation start=\"7\" end=\"13\" label=\"FRUIT\" color=\"color\" />\n" +
                "    </annotations>\n" +
                "</Document>";
        Document expectedDocument = new Document(UUID.fromString("28aa4362-7229-44bd-80fa-6d0495c97100"), "Document Name", "I love apples!", new Annotations(Arrays.asList(new Annotation(7, 13, "FRUIT", "color"))));
        Document actualDocument = Converter.convertXmlToDocument(xml);
        assertEquals(expectedDocument, actualDocument, "Converted Document does not match expected Document");
    }
    @Test
    void testConvertDocumentToXml() throws JAXBException {
        String expectedXml = "" +
                "<Document guid=\"93cd59f4-7166-4bd2-8b59-3f8e456b702f\">\n" +
                "    <name>DocumentName</name>\n" +
                "    <text>Iloveapples!</text>\n" +
                "    <annotations>\n" +
                "        <annotation start=\"7\" end=\"13\" label=\"FRUIT\" color=\"1\" />\n" +
                "    </annotations>\n" +
                "</Document>";
        Document document = new Document(UUID.fromString("93cd59f4-7166-4bd2-8b59-3f8e456b702f"), "Document Name","I love apples!", new Annotations(Arrays.asList(new Annotation(7, 13, "FRUIT","1"))));
        String actualXml = Converter.convertDocumentToXml(document);
        String cleanedActualXml = actualXml.replaceAll("\\s+", "");
        String cleanedExpectedXml = expectedXml.replaceAll("\\s+", "");
        assertEquals(cleanedExpectedXml, cleanedActualXml);
    }
    @Test
    public void testConvertDocxToDocument() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Test.docx").getFile());
        String filePath = file.getPath();
        String expectedText = "This is the expected text in the docx file. This is the expected text in the docx file2.";
        Document result = Converter.convertDocxToDocument(filePath);
        assertEquals(expectedText, result.getText());
        System.out.println(result.getText());
        assertTrue(result.getAnnotations().getAnnotations().isEmpty());
    }
    @Test
    public void testConvertTxtToDocuments() {
        String txt =
                "I love apples and oranges!\n" +
                        "Bananas and grapes are good for your health.\n" +
                        "Cherries and strawberries are grown in many parts of the world.\n";
        Document expected = new Document(UUID.randomUUID(), "Document Name",txt, new Annotations(new ArrayList<>()));
        try {
            Document actual = convertTxtToDocuments(txt);
            assertEquals(expected.getText(), actual.getText());
            assertTrue(actual.getAnnotations().getAnnotations().isEmpty());
        } catch (Exception e) {
            fail("Error during conversion", e);
        }
    }
}
