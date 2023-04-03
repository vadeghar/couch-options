package com.couchoptions.repository;

import com.couchoptions.entity.OptionChain;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Collection("OptionChain")
public interface NseOptionsRepository extends CouchbaseRepository<OptionChain, UUID> {
    @Query("#{#n1ql.selectEntity}  where upper(symbol) = upper($1) order by lastUpdatedTs desc LIMIT 1")
    OptionChain getLastInserted(String symbol);

    @Query("#{#n1ql.selectEntity} where " +
            "#{#n1ql.filter} " +
            "and expiryDate = $1 " +
            "and upper(symbol) = upper($2) " +
            "and realtimeUpdatedTs >= $3 " +
            "and realtimeUpdatedTs <= $4 " +
            "order by lastUpdatedTs asc")
    List<OptionChain> getOptionChainData(long expiryDate, String symbol, long startTs, long endTs);

}
