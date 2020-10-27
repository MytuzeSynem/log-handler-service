package com.example.logshandlerservice.config;

import lombok.extern.slf4j.Slf4j;
import org.hsqldb.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Slf4j
@Configuration
public class    DataSourceConfiguration {

    @Value("${hsqldb.server.port}")
    private int serverPort;

    @Value("${hsqldb.server.database.name}")
    private String databaseName;

    @Value("${hsqldb.server.data}")
    private String serverData;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server hsqldbServer() {

        Server server = new Server();
        server.setDatabaseName(0, databaseName);
        server.setDatabasePath(0, serverData);
        server.setPort(serverPort);
        server.setLogWriter(slf4jPrintWriter());
        server.setErrWriter(slf4jPrintWriter());

        return server;
    }

    private PrintWriter slf4jPrintWriter() {
        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream()) {

            @Override
            public void println(final String x) {
                log.debug(x);
            }
        };
        return printWriter;
    }
}