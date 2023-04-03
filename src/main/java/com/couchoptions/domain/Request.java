package com.couchoptions.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {
    private String symbol;
    private String expiry;
    private long fromTs;
    private long toTs;
}
