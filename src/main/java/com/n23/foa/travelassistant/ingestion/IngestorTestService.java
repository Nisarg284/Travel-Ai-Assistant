package com.n23.foa.travelassistant.ingestion;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class IngestorTestService {

    private final EmbeddingStoreIngestor ingestor;
    private final TravelSectionExtractor sectionExtractor;

    public IngestorTestService(EmbeddingStoreIngestor ingestor, TravelSectionExtractor sectionExtractor) {
        this.ingestor = ingestor;
        this.sectionExtractor = sectionExtractor;
    }

    public void ingest() throws IOException {

//        Path path = Path.of("C:/Users/Nisarg/OneDrive/Desktop/java/Langchain4j/Projects/Travel Assistant/knowledge-base/destinations/goa.md");
        Path folderPath = Paths.get("C:\\Users\\Nisarg\\OneDrive\\Desktop\\java\\Langchain4j\\Projects\\Travel Assistant\\knowledge-base\\destinations");

//        Files.list(folderPath)
//                .filter(path -> path.toString().endsWith(".md"))
//                .forEach(
//                        path -> {
//                            try {
//                                String mdFile = Files.readString(path);
//                                List<TravelSection> sections = sectionExtractor.extract(mdFile);
//                                List<Document> documents = sections.stream().map(
//                                        ts -> {
//                                            Metadata metadata = new Metadata(Map.of(
//                                                    "destination", ts.destination(),
//                                                    "section", ts.section()
//                                            ));
//                                            return Document.document(ts.content(), metadata);
//                                        }
//                                ).toList();
//                                ingestor.ingest(documents);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                );

        List<Document> allDocuments;
        try(var paths = Files.list(folderPath)) {



            allDocuments = paths
                    .filter(path -> path.toString().endsWith(".md"))
                    .flatMap(path -> {
                        try {
                            String markdownFile = Files.readString(path);

                            return sectionExtractor.extract(markdownFile)
                                    .stream()
                                    .map(ts -> {
                                        Metadata metadata = new Metadata(Map.of(
                                                "destination",ts.destination(),
                                                "section",ts.section()
                                        ));
                                        return Document.document(ts.content(),metadata);
                                    });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }

        ingestor.ingest(allDocuments);


//        String markdownFile = Files.readString(path);
//
//
//        List<Document> documents = sectionList.stream()
//                .map(
//                        ts -> {
//                            Metadata metadata = new Metadata(Map.of(
//                                    "destination", ts.destination(),
//                                    "section", ts.section()));
//
//                            return Document.document(ts.content(), metadata);
//                        }
//                ).toList();
//
//        ingestor.ingest(documents);
    }
}
