package com.n23.foa.travelassistant.ingestion;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IngestorTestService {

    private final EmbeddingStoreIngestor ingestor;
    private final TravelSectionExtractor sectionExtractor;

    public IngestorTestService(EmbeddingStoreIngestor ingestor, TravelSectionExtractor sectionExtractor) {
        this.ingestor = ingestor;
        this.sectionExtractor = sectionExtractor;
    }

    public int ingest() throws IOException {
        Path folderPath = Paths.get("knowledge-base", "destinations");
        if (!Files.exists(folderPath)) {
            log.warn("Knowledge base directory does not exist at: {}", folderPath.toAbsolutePath());
            return 0;
        }

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
                                                "destination", ts.destination() != null ? ts.destination() : "",
                                                "section", ts.section() != null ? ts.section() : ""
                                        ));
                                        return Document.document(ts.content(), metadata);
                                    });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }

        ingestor.ingest(allDocuments);
        log.info("Successfully ingested {} sections into vector store", allDocuments.size());
        return allDocuments.size();
    }
}
