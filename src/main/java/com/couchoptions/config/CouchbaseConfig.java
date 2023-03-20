package com.couchoptions.config;

import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseOperations;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${application.couchbase.scope}")
    private String scope;


    @Override
    public String getConnectionString() {
        return "172.17.0.2";
    }

    @Override
    public String getUserName() {
        return "Administrator";
    }

    @Override
    public String getPassword() {
        return "Administrator";
    }

    @Override
    public String getBucketName() {
        return "bucket-0";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    protected String getScopeName() {
        return scope;
    }
    public QueryScanConsistency getDefaultConsistency() {
        return QueryScanConsistency.REQUEST_PLUS;
    }
}
