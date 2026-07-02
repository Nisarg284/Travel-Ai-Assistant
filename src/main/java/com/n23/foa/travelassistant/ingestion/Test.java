package com.n23.foa.travelassistant.ingestion;

import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Test {

    private final TravelIngestionService ingestionService;
    private final IngestorTestService ingestorTestService;


    public Test(TravelIngestionService ingestionService, IngestorTestService ingestorTestService) {
        this.ingestionService = ingestionService;
        this.ingestorTestService = ingestorTestService;
    }


    public void test() throws IOException {
        ingestionService.uploadFile();
    }


    public void test2() throws IOException {
        ingestorTestService.ingest();
    }
}
