package com.couchoptions.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Value("${application.webClient.nse.baseUrl}")
    private String nseBaseUrl;
    @Value("${application.webClient.nse.apiVersion}")
    private String apiVersion;

    @Bean("simpleWebClient")
    public WebClient getSimpleWebClient() {
        return WebClient.builder()
                .baseUrl(nseBaseUrl)
                .exchangeStrategies(buildExchangeStrategies())
                .build();
    }


    @Bean("nseWebClient")
    public WebClient getNseWebClient() {

        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, "*/*")
                .defaultHeader(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.4")
                .baseUrl(nseBaseUrl+apiVersion)
                .clientConnector(buildClientHttpConnector())
                .filter(buildRetryExchangeFunction())
                .exchangeStrategies(buildExchangeStrategies())
                .build();
    }

    private ExchangeStrategies buildExchangeStrategies() {
        final int size = 16 * 1024 * 1024;
        return ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
    }

    private ExchangeFilterFunction buildRetryExchangeFunction() {
        return ((request, next) -> next.exchange(request)
                .flatMap(clientResponse -> Mono.just(clientResponse)
                        .filter(response -> clientResponse.statusCode().isError())
                        .flatMap(response -> clientResponse.createException())
                        .flatMap(Mono::error)
                        .thenReturn(clientResponse)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(15000)))
                )
        );
    }

    private ClientHttpConnector buildClientHttpConnector() {
        return new ReactorClientHttpConnector(
          HttpClient.create()
                  .doOnConnected(
                          conn -> conn.addHandlerFirst(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                  ).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
        );
    }

}
