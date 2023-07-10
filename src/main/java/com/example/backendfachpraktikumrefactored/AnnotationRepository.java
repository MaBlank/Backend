package com.example.backendfachpraktikumrefactored;
import com.example.backendfachpraktikumrefactored.Model.Annotation;
import com.example.backendfachpraktikumrefactored.Model.Annotations;
import com.example.backendfachpraktikumrefactored.Model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface AnnotationRepository extends MongoRepository<Document, String> {

}
