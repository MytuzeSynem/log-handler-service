package com.example.logshandlerservice.persisance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOG_EVENTS")
@Getter
@Setter
@ToString
public class LogEvent {

    @Id
    private Integer id;
    private int duration;
    private boolean alert;
    private String eventType;
    private String host;
}
