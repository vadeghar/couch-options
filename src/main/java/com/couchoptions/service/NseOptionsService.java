package com.couchoptions.service;

import com.couchoptions.client.domain.CeOrPe;
import com.couchoptions.client.domain.OptionsData;
import com.couchoptions.client.service.NseClientService;
import com.couchoptions.entity.Option;
import com.couchoptions.entity.OptionChain;
import com.couchoptions.entity.OptionData;
import com.couchoptions.repository.NseOptionsRepository;
import com.couchoptions.utils.AppUtils;
import com.couchoptions.utils.Constants;
import com.couchoptions.utils.TrackExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NseOptionsService {
    @Autowired
    private NseClientService client;

    @Autowired
    private NseOptionsRepository nseOptionsRepository;

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    @PostConstruct
    public void createPrimaryIndex() {

    }

    @TrackExecutionTime
    @Scheduled(fixedDelayString = "PT3M")
    public void saveOptionsChain() {
        long start = System.currentTimeMillis();
        log.info("----------------------------------------------------------------------------------------------------------------------------");
        log.info("    "+LocalDateTime.now().format(AppUtils.DD_MMM_YYYY_HH_MM_SS)+" ");
        log.info("----------------------------------------------------------------------------------------------------------------------------");
        log.info("Start saveOptionsChain of NseOptionsService");
        if(LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.parse(AppUtils.START_TIME))
                || LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.parse(AppUtils.END_TIME ))) {
            log.info("Out of hours "+LocalDateTime.now().format(AppUtils.DD_MMM_YYYY_HH_MM_SS));
            return;
        }
        OptionsData response;
        try{
            response = client.getOptionChainData(Constants.NIFTY);
            if(response == null || response.getRecords() == null) {
                log.error("******************* There is an Error, Response is null");
                long end = System.currentTimeMillis();
                log.info("*** End saveOptionsChain of NseOptionsService in "+ (end - start) + " milli seconds at " + LocalDateTime.now().format(AppUtils.DD_MMM_YYYY_HH_MM_SS));
                return;
            }
            log.info("Current updated at NSE: "+response.getRecords().getTimestamp());
            OptionChain optionChain = buildOptionChain(response);
            OptionChain lastInserted = nseOptionsRepository.getLastInserted();
            if(lastInserted != null) {
                if(!lastInserted.getRealtimeUpdatedTs().isEqual(optionChain.getRealtimeUpdatedTs())) {
                    log.info("Last inserted NSE timestamp "+lastInserted.getRealtimeUpdatedTs().format(AppUtils.DD_MMM_YYYY_HH_MM_SS)+ " Price: "+lastInserted.getUnderlyingValue());
                    log.info("Current inserting NSE timestamp "+optionChain.getRealtimeUpdatedTs().format(AppUtils.DD_MMM_YYYY_HH_MM_SS)+ " Price: "+optionChain.getUnderlyingValue());
                    nseOptionsRepository.save(optionChain);
                } else {
                    log.info("******************** With this timestamp record already exists (NOT INSERTING IN COUCHBASE) ");
                }
            } else {
                log.info("Current inserting NSE timestamp "+optionChain.getRealtimeUpdatedTs().format(AppUtils.DD_MMM_YYYY_HH_MM_SS)+ " Price: "+optionChain.getUnderlyingValue());
                nseOptionsRepository.save(optionChain);
            }

            long end = System.currentTimeMillis();
            log.info("End saveOptionsChain of NseOptionsService in "+(end - start) + " milli seconds at " + LocalDateTime.now().format(AppUtils.DD_MMM_YYYY_HH_MM_SS));
            log.info("----------------------------------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in NSE Option Chain Service: "+e.getMessage());
        }
    }

    private OptionChain buildOptionChain(OptionsData optionData) {
        List<Option> options = buildOptions(optionData);
        return OptionChain.builder()
                .id(UUID.randomUUID())
                .symbol(optionData.getFiltered().getData().get(0).getPE().getUnderlying())
                .underlyingValue(optionData.getRecords().getUnderlyingValue())
                .realtimeUpdatedTs(LocalDateTime.parse(optionData.getRecords().getTimestamp(), AppUtils.DD_MMM_YYYY_HH_MM_SS))
                .totCeOI(optionData.getFiltered().getCE().getTotOI())
                .totCeVol(optionData.getFiltered().getCE().getTotVol())
                .totPeOI(optionData.getFiltered().getPE().getTotOI())
                .totPeVol(optionData.getFiltered().getPE().getTotVol())
                .optionList(options)
                .lastUpdatedTs(LocalDateTime.now())
                .build();
    }

    private List<Option> buildOptions(OptionsData optionData) {
        return optionData.getFiltered().getData()
                .stream()
                .map(data -> {
                    return Option.builder()
                            .id(UUID.randomUUID())
                            .strikePrice(data.getStrikePrice())
                            .ce(buildOptionData(data.getCE()))
                            .pe(buildOptionData(data.getPE()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private OptionData buildOptionData(CeOrPe ceOrPe) {
        return OptionData.builder()
                .id(UUID.randomUUID())
                .change(ceOrPe.getChange())
                .changeInOpenInterest(ceOrPe.getChangeinOpenInterest())
                .impliedVolatility(ceOrPe.getImpliedVolatility())
                .openInterest(ceOrPe.getOpenInterest())
                .lastPrice(ceOrPe.getLastPrice())
                .percChangeInOpenInterest(ceOrPe.getPchangeinOpenInterest())
                .percChange(ceOrPe.getPChange())
                .totalBuyQuantity(ceOrPe.getTotalBuyQuantity())
                .totalSellQuantity(ceOrPe.getTotalSellQuantity())
                .totalTradedVolume(ceOrPe.getTotalTradedVolume())
                .build();
    }
}
