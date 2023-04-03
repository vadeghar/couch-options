package com.couchoptions.domain;

import com.couchoptions.entity.OptionChain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {
    private String currentExpiry;
    private List<String> expiryDates;
    private List<OptionChain> data;
}
