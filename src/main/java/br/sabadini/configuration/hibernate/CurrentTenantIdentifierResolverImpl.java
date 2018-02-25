package br.sabadini.configuration.hibernate;

import br.sabadini.configuration.TenantThreadLocalStorage;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Deprecated
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);
    @Override
    public String resolveCurrentTenantIdentifier() {
        String schema = TenantThreadLocalStorage.SCHEMA_ID.get();
        LOGGER.info("schemaid -> " + schema);

        return Optional.ofNullable(schema).orElse("public");
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
