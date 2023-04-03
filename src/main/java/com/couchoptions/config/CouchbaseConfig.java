package com.couchoptions.config;

import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${application.couchbase.scope}")
    private String scope;
    @Value("${application.couchbase.host}")
    private String host;
    @Value("${application.couchbase.username}")
    private String username;
    @Value("${application.couchbase.password}")
    private String password;
    @Value("${application.couchbase.bucket}")
    private String bucket;


    @Override
    public String getConnectionString() {
//        return "172.17.0.2";
        return host;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucket;
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
