package com.example.backendfachpraktikumrefactored.Model;

import lombok.*;
import javax.xml.bind.annotation.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {
    @XmlAttribute
    private UUID guid;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "text")
    private String text;
    @XmlElement(name = "annotations")
    private Annotations annotations;
}
