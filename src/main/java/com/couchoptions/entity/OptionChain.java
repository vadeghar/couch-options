package com.couchoptions.entity;

import com.couchoptions.config.CustomLocalDateSerializer;
import com.couchoptions.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@ToString
public class OptionChain {
    private UUID id;
    private String symbol;
    private double underlyingValue;
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate expiryDate;
    private long totCeOI;
    private long totCeVol;
    private long totPeOI;
    private long totPeVol;
    private List<Option> optionList;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime lastUpdatedTs;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime realtimeUpdatedTs;
}
