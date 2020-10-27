package com.example.logshandlerservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties
public class RawLogEvent implements Serializable {

    private Integer id;
    private String state;
    private String type;
    private String host;
    private long timestamp;
}
