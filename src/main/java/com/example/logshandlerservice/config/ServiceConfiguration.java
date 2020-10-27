package com.example.logshandlerservice.config;

import com.example.logshandlerservice.service.dto.RawLogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class ServiceConfiguration {

    @Value("${shared.blocking.queue.size}")
    private int sharedBlockingQueueSize;

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public BlockingQueue<List<RawLogEvent>> blockingQueue(){
        return new LinkedBlockingQueue<>(sharedBlockingQueueSize);
    }

}
