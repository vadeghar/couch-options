package com.couchoptions.controller;

import com.couchoptions.CouchOptionsApplication;
import com.couchoptions.config.MillisOrLocalDateDeserializer;
import com.couchoptions.config.MillisOrLocalDateTimeDeserializer;
import com.couchoptions.domain.OptionChainList;
import com.couchoptions.domain.Request;
import com.couchoptions.domain.Response;
import com.couchoptions.entity.OptionChain;
import com.couchoptions.repository.NseOptionsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = CouchOptionsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OptionChainControllerTest {
    @LocalServerPort
    int port;
    private String baseUrl;

    @Autowired
    NseOptionsRepository repository;

    @PostConstruct
    void init() {
        baseUrl = "http://localhost:"+port;
    }

    @BeforeEach
    void initEach() {
        RestAssured.port = port;
    }

    @Test
    void optionChainTest() {
        Request request = new Request();
        request.setSymbol("NIFTY");
        request.setExpiry("29-Mar-2023");
        Response response  = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/option-chain")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Response.class);
        System.out.println("**************************************");

        System.out.println("Response Size:"+ response);
        System.out.println("**************************************");


    }

    //@Test
    void saveData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new MillisOrLocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new MillisOrLocalDateDeserializer());
        mapper.registerModule(javaTimeModule);
        TypeReference<List<OptionChainList>> mapType = new TypeReference<List<OptionChainList>>() {};
//        ClassPathResource res = new ClassPathResource("resources/couchbase_data_backup.json");
//        File file = new File(res.getPath());
//        File file = ResourceUtils.getFile("classpath:resources/couchbase_data_backup.json");

//        List<OptionChain> jsonToPersonList = mapper.readValue(file, mapType);
//        List<OptionChain> jsonToPersonList = mapper.readValue("/", mapType);
        ClassPathResource resource = new ClassPathResource("/couchbase_data_backup.json", CouchOptionsApplication.class);
        try (InputStream inputStream = resource.getInputStream()) {
            String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//            System.out.println("***********************\n"+string);
        List<OptionChainList> jsonToPersonList = mapper.readValue(string, mapType);
                    System.out.println("\n2. Convert JSON to List of person objects :"+jsonToPersonList.size());
                    List<OptionChain> optionChainList = jsonToPersonList.stream().map(x -> {
                        OptionChain oc = x.getOptionChain();
                        oc.setId(UUID.randomUUID());
                        return oc;
                    }).collect(Collectors.toList());
            System.out.println("FINAL LIST COUNT: "+optionChainList.size());
            repository.saveAll(optionChainList);

        }
    }



}
