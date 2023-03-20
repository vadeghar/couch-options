package com.couchoptions.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.Scope;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class OptionChain {
    private UUID id;
    private String symbol;
    private double underlyingValue;
    private LocalDate expiryDate;
    private long totCeOI;
    private long totCeVol;
    private long totPeOI;
    private long totPeVol;
    private List<Option> optionList;
    private LocalDateTime lastUpdatedTs;
    private LocalDateTime realtimeUpdatedTs;
}
