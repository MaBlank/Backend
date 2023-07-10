package com.example.backendfachpraktikumrefactored.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Annotation {
    @XmlAttribute
    private Integer start;
    @XmlAttribute
    private Integer end;
    @XmlAttribute
    private String label;
    @XmlAttribute
    private String color;
}
