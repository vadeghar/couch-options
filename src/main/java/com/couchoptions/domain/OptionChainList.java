package com.couchoptions.domain;

import com.couchoptions.entity.OptionChain;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionChainList {
    @JsonProperty("OptionChain")
    private OptionChain optionChain;
}
