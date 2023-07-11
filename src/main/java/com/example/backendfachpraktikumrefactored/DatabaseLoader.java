package com.example.backendfachpraktikumrefactored;

import com.example.backendfachpraktikumrefactored.Model.Annotation;
import com.example.backendfachpraktikumrefactored.Model.Annotations;
import com.example.backendfachpraktikumrefactored.Model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private final AnnotationRepository repository;
    @Autowired
    public DatabaseLoader(AnnotationRepository repository) {
        this.repository = repository;
    }
    @Override
    public void run(String... strings) {
        String text = "Im August sind wir in den Urlaub nach Barcelona, Spanien, gefahren. Unser Flug startete um 11 Uhr morgens. Wir hatten eine aufregende Reise vor uns und waren voller Vorfreude auf all die Abenteuer, die uns erwarten sollten.Als wir am Flughafen ankamen, war die Atmosphäre lebhaft und voller Aufregung. Menschen unterschiedlicher Nationalitäten eilten durch die Terminals, und das Summen der Gespräche und das Klappern der Rollkoffer erfüllten die Luft. Nachdem wir eingecheckt und unsere Bordkarten erhalten hatten, begaben wir uns zur Sicherheitskontrolle. Das Aufgeben unseres Gepäcks verlief reibungslos, und bald darauf waren wir bereit, das Flugzeug zu besteigen.";
        List<Annotation> annotations = new ArrayList<>();
        annotations.add(new Annotation(4, 9, "Date", "#0d6efd"));
        annotations.add(new Annotation(36, 45, "City", "#dc3545"));
        annotations.add(new Annotation(47, 52, "Country", "#198754"));
        annotations.add(new Annotation(77, 85, "Time", "#6c757d"));

        Annotations annotationList = new Annotations(annotations);
        Document document = new Document(UUID.randomUUID(), "DocumentName", text, annotationList);

        this.repository.save(document);
    }
}
