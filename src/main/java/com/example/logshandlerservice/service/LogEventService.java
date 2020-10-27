package com.example.logshandlerservice.service;

import com.example.logshandlerservice.persisance.model.LogEvent;
import com.example.logshandlerservice.persisance.repository.LogEventRepository;
import com.example.logshandlerservice.service.dto.RawLogEvent;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
@AllArgsConstructor
public class LogEventService {

    private final BlockingQueue<List<RawLogEvent>> blockingQueue;
    private final List<RawLogEvent> poisonPill;
    private final LogEventRepository logEventRepository;

    public void processFetchedLogs(){

        try {
            while (true) {

                List<RawLogEvent> matchedLogEvents = blockingQueue.take();

                if (matchedLogEvents == poisonPill) {
                    break;
                }

                LogEvent logEvent = createLogEventEntity(matchedLogEvents);
                saveLogEvent(logEvent);

                }

            log.info("End of data processing");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private LogEvent createLogEventEntity(List<RawLogEvent> matchedLogEvents) {

        int duration = Math.abs(Math.toIntExact((matchedLogEvents.get(0).getTimestamp() -
                matchedLogEvents.get(1).getTimestamp())));

        boolean alert = false;

       if (5 < duration){
            alert = true;
        }

        LogEvent logEvent = new LogEvent();
        logEvent.setId(matchedLogEvents.get(0).getId());
        logEvent.setEventType(matchedLogEvents.get(0).getType());
        logEvent.setHost(matchedLogEvents.get(0).getHost());
        logEvent.setDuration(duration);
        logEvent.setAlert(alert);

        return logEvent;
    }

    public void saveLogEvent(LogEvent logEvent){

        logEventRepository.save(logEvent);
        log.debug("Thread {}, saving object to database {}", Thread.currentThread().getName(), logEvent);

    }
}
