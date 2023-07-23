# Funktionsweise Backend

Bitte Reihenfolge beachten:

1. Schritt
   Docker-Compose-yaml db starten
   Hinweis: Aufgrund der Speicherung der Daten als JSON wurde diese NoSQL-Datenbank verwendet
2. Schritt
   mvn clean install
   Hinweis: Das Projekt wird hierdurch gebaut und die Tests ausgeführt
3. Schritt
   app starten über BackendFachpraktikumRefactoredApplication
   Hinweis: Hierdurch wird die SpringBoot-App gestartet
4. Schritt
   GET http://localhost:8080/api/projects
   Hierüber sind die Dummy-Projekte verfügbar und die Verfügbarkeit des Backend kann getestet werden

   Abschließender Hinweis:

   Die Anwendung ist gerade auf das lokale Laufen ausgelegt. Für eine Ausführung in Azure AKS müssen im Frontend die
   URLS entsprechend der in Azure verfügbar gemachten IP-Adressen angepasst werden

