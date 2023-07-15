package com.example.backendfachpraktikumrefactored;
import com.example.backendfachpraktikumrefactored.Model.Annotation;
import com.example.backendfachpraktikumrefactored.Model.Annotations;
import com.example.backendfachpraktikumrefactored.Model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AnnotationRepository extends MongoRepository<Document, String> {
    @Query("{ 'guid' : ?0 }")
    Optional<Document> findByGuid(UUID guid);
    @DeleteQuery("{ 'guid' : ?0 }")
    void deleteByGuid(UUID guid);
}
