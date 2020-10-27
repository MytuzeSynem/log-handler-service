package com.example.logshandlerservice.adapter;

import com.example.logshandlerservice.service.dto.RawLogEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Slf4j
@AllArgsConstructor
public class LogFileReader {

    private final String filePath;
    private final BlockingQueue<List<RawLogEvent>> blockingQueue;
    private final List<RawLogEvent> poisonPill;
    private final ObjectMapper objectMapper;

    public void readFile() {

        final Map<Integer, RawLogEvent> rawLogEventsStringLineCache = new HashMap<>();

        log.info("Starting reading log file {}", filePath);

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath))) {

            matchLogEventsWithSameIdAndPutItToQueue(bufferedReader, rawLogEventsStringLineCache);

        } catch (JsonProcessingException e) {
            log.error("Json processing Error {}", e.getMessage());

        } catch (IOException e) {
            log.error("Reading file error {}", e.getMessage());
        }

    }

    private void matchLogEventsWithSameIdAndPutItToQueue(BufferedReader bufferedReader, Map<Integer, RawLogEvent> rawLogEventsStringLineCache) throws IOException {

        String line;

        RawLogEvent rawLogEvent;

        while ( (line = bufferedReader.readLine()) != null) {

            rawLogEvent = objectMapper.readValue(line, RawLogEvent.class);

            if (!rawLogEventsStringLineCache.containsKey(rawLogEvent.getId())) {
                rawLogEventsStringLineCache.put(rawLogEvent.getId(), rawLogEvent);
            }
            else {

                final List<RawLogEvent> rawLogEventMatchingPair = new ArrayList<>();

                rawLogEventMatchingPair.add(rawLogEventsStringLineCache.get(rawLogEvent.getId()));
                rawLogEventMatchingPair.add(rawLogEvent);

                rawLogEventsStringLineCache.remove(rawLogEvent.getId());

                    try {
                        blockingQueue.put(rawLogEventMatchingPair);
                    } catch (InterruptedException e) {
                        log.error("Error putting element into Queue: {}", e.getMessage());
                    }
            }
        }

        try {
            blockingQueue.put(poisonPill);
        } catch (InterruptedException e) {
            log.error("Error putting poison pill into Queue: {}", e.getMessage());
        }
    }
}
