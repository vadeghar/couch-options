package com.couchoptions.repository;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchoptions.entity.OptionChain;
import org.springframework.data.couchbase.core.query.N1qlJoin;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Collection("OptionChain")
public interface NseOptionsRepository extends CouchbaseRepository<OptionChain, UUID> {
    @Query("#{#n1ql.selectEntity} order by lastUpdatedTs desc LIMIT 1")
    OptionChain getLastInserted();

}
