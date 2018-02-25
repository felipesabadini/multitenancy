package br.sabadini.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantAwareRoutingSource extends AbstractRoutingDataSource {
    private static final Logger LOG = LoggerFactory.getLogger(TenantAwareRoutingSource.class);


    @Override
    protected Object determineCurrentLookupKey() {
        LOG.info("TenantID -> ".concat(TenantThreadLocalStorage.tenanctID.get()));
        return TenantThreadLocalStorage.tenanctID.get();
    }
}
