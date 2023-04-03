package com.couchoptions.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@ToString
public class Option {
    @Id
    private UUID id;
    private int strikePrice;
    private OptionData ce;
    private OptionData pe;
}
