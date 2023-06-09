package com.couchoptions.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class OptionData {
    @Id
    private UUID id;
    private double openInterest;
    private int changeInOpenInterest;
    private double percChangeInOpenInterest;
    private int totalTradedVolume;
    private double impliedVolatility;
    private double lastPrice;
    private double change;
    private double percChange;
    private int totalBuyQuantity;
    private int totalSellQuantity;
}
