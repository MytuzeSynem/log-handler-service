package com.example.logshandlerservice.persisance.repository;

import com.example.logshandlerservice.persisance.model.LogEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEventRepository extends CrudRepository<LogEvent, Integer> {}
