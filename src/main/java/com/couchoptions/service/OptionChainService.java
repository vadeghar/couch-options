package com.couchoptions.service;

import com.couchoptions.domain.Request;
import com.couchoptions.domain.Response;
import com.couchoptions.entity.ExpiryDates;
import com.couchoptions.repository.ExpiryDatesRepository;
import com.couchoptions.repository.NseOptionsRepository;
import com.couchoptions.utils.AppUtils;
import com.couchoptions.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class OptionChainService {

    private final NseOptionsRepository nseOptionsRepository;
    private final ExpiryDatesRepository expiryDatesRepository;

    public OptionChainService(NseOptionsRepository nseOptionsRepository, ExpiryDatesRepository expiryDatesRepository) {
        this.nseOptionsRepository = nseOptionsRepository;
        this.expiryDatesRepository = expiryDatesRepository;
    }

    public Response getOptionChain(Request request) {
        Response response = new Response();
        ExpiryDates expiryDates = expiryDatesRepository.getLastInserted();
        response.setCurrentExpiry(expiryDates.getCurrentExpiry().format(AppUtils.DD_MMM_YYYY));
        response.setExpiryDates(expiryDates.getExpiryDates());
        LocalDate expiry = LocalDate.parse(request.getExpiry(), AppUtils.DD_MMM_YYYY);
        LocalDateTime startTime = LocalDate.now().atTime(9, 15);
        LocalDateTime endTime = LocalDate.now().atTime(15, 30);
        if(0 == request.getFromTs())
            request.setFromTs(startTime.atZone(ZoneId.of(Constants.TIME_ZONE_ID)).toInstant().toEpochMilli());
        if(0 == request.getToTs())
            request.setToTs(endTime.atZone(ZoneId.of(Constants.TIME_ZONE_ID)).toInstant().toEpochMilli());
        long ms = expiry.atStartOfDay(ZoneId.of(Constants.TIME_ZONE_ID)).toEpochSecond()*1000;
        System.out.println("Expiry m-seconds " + request);

        log.info("Expiry: "+ms);
        response.setData(nseOptionsRepository.getOptionChainData(ms, request.getSymbol(), request.getFromTs(), request.getToTs()));
        return response;
    }
}
