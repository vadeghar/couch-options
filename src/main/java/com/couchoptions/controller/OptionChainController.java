package com.couchoptions.controller;

import com.couchoptions.domain.Request;
import com.couchoptions.domain.Response;
import com.couchoptions.service.OptionChainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionChainController {
    private final OptionChainService optionChainService;

    public OptionChainController(OptionChainService optionChainService) {
        this.optionChainService = optionChainService;
    }

    @PostMapping("option-chain")
    public ResponseEntity<Response> getOptionChainData(@RequestBody Request request) {
        return ResponseEntity.ok(optionChainService.getOptionChain(request));
    }

}
