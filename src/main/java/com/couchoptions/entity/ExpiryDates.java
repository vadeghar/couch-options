package com.couchoptions.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ExpiryDates {
    private UUID id;
    private LocalDate currentExpiry;
    private List<String> expiryDates;
    private LocalDateTime lastUpdatedTs;
}
