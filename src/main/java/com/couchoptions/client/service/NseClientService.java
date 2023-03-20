package com.couchoptions.client.service;

import com.couchoptions.client.domain.OptionsData;
import com.couchoptions.exceptions.NseClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class NseClientService {
    @Autowired
    @Qualifier("nseWebClient")
    private WebClient nseWebClient;
    @Autowired
    @Qualifier("simpleWebClient")
    private WebClient simpleWebClient;

    public OptionsData getOptionChainData(String symbol) throws Exception {
        long start = System.currentTimeMillis();
        log.info("Start getOptionChain of NseClient");
        try {
            String url = "/option-chain-indices?symbol="+symbol;
            OptionsData response = nseWebClient.get()
                    .uri(url)
                    .header("Cookie", getNseCookie())
//                    .cookie("Cookie", )
                    .retrieve()
                    .onStatus(HttpStatus::is5xxServerError, errorResponse ->
                            errorResponse.bodyToMono(String.class).map(NseClientException::new)
                    )
                    .bodyToMono(OptionsData.class)
                    .block();
            long end = System.currentTimeMillis();
            log.info("End getOptionChain of NseClient in "+(end - start) + " milli seconds");
            return response;
        } catch (NseClientException e) {
            log.error("******************** Client Error: "+e.getMessage()+" ***********");
            return getOptionChainData(symbol);
        } catch (Exception e) {
            log.error("******************** RETURNING NULL VALUE, SEE THE ERROR BELOW ***********");
            e.printStackTrace();
            return null;
        }
    }



    public String getNseCookie() {
        long start = System.currentTimeMillis();
        log.info("Start getNseCookie of NseClient");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("User-Agent", "PostmanRuntime/7.28.4");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = simpleWebClient.get()
                        .uri("/")
                        .retrieve()
                        .toEntity(String.class)
                        .block();
//                restTemplate.exchange("https://www.nseindia.com", HttpMethod.GET, entity, String.class);
        StringBuilder sb = new StringBuilder();
        Optional<Map.Entry<String, List<String>>> cookie =  response.getHeaders().entrySet().stream().filter(es-> es.getKey().equalsIgnoreCase("Set-Cookie")).findFirst();
        if(cookie.isPresent()) {
            Map.Entry<String, List<String>> entry = cookie.get();
            for(String s : entry.getValue()) {
                if(s.contains("nsit")) {
                    if(sb.toString().length() > 0)
                        sb.append(" ");
                    sb.append(s.substring(s.indexOf("nsit"), s.indexOf(";")+1));
                }
                if(s.contains("nseappid")) {
                    if(sb.toString().length() > 0)
                        sb.append(" ");
                    sb.append(s.substring(s.indexOf("nseappid"), s.indexOf(";")+1));
                }
            }
        }
        long end = System.currentTimeMillis();
        log.info("End getNseCookie of NseClient in " +(end - start) + " milli seconds");
        return sb.toString();
    }

}
