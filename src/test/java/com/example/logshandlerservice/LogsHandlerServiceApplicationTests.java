package com.example.logshandlerservice;

import com.example.logshandlerservice.persisance.repository.LogEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class LogsHandlerServiceApplicationTests {

	private LogEventRepository logEventRepository;

	@Test
	void contextLoads() {
	}

}
