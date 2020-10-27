package com.example.logshandlerservice;

import com.example.logshandlerservice.adapter.LogFileReader;
import com.example.logshandlerservice.persisance.repository.LogEventRepository;
import com.example.logshandlerservice.service.LogEventService;
import com.example.logshandlerservice.service.dto.RawLogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@AllArgsConstructor
@Profile("!test")
public class LogsHandlerServiceApplication implements CommandLineRunner {

	private final BlockingQueue<List<RawLogEvent>> blockingQueue;
	private final ObjectMapper objectMapper;
	private final LogEventRepository logEventRepository;

	public static void main(String[] args) {
		SpringApplication.run(LogsHandlerServiceApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {

		String filePath = args[0];

		final int numberOfThreads = Runtime.getRuntime().availableProcessors();

		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

		List<RawLogEvent> poisonPill = Collections.singletonList(new RawLogEvent());

		Runnable readLogsFromFile = () -> {
			LogFileReader logFileReader = new LogFileReader(filePath, blockingQueue, poisonPill, objectMapper);
			logFileReader.readFile();
		};

		Runnable processLogEvent = null;

		for (int i = 1; i < numberOfThreads; i++){
			 processLogEvent = () -> {
				LogEventService logEventService = new LogEventService(blockingQueue, poisonPill, logEventRepository);
				logEventService.processFetchedLogs();
			};
		}

		Future<?> readLogs = executor.submit(readLogsFromFile);
		Future<?> writeLogs =  executor.submit(processLogEvent);
		readLogs.get();
		writeLogs.get();
		executor.shutdown();

	}
}
