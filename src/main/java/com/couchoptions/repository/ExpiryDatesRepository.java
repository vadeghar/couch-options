package com.couchoptions.repository;

import com.couchoptions.entity.ExpiryDates;
import com.couchoptions.entity.OptionChain;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Collection("ExpiryDates")
public interface ExpiryDatesRepository extends CouchbaseRepository<ExpiryDates, UUID> {
    @Query("#{#n1ql.selectEntity} order by lastUpdatedTs desc LIMIT 1")
    ExpiryDates getLastInserted();

}
