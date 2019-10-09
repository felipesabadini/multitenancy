package br.sabadini.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class TenantHikariDataSource extends HikariDataSource {
    private static final Logger LOG = LoggerFactory.getLogger(TenantHikariDataSource.class);
    public TenantHikariDataSource() {
        super();
    }

    public TenantHikariDataSource(HikariConfig configuration) {
        super(configuration);
    }

    @Override
    @SuppressWarnings("all")
    public Connection getConnection() throws SQLException {
        LOG.info("Database[{}] Schema[{}]", TenantThreadLocalStorage.tenanctID.get(), TenantThreadLocalStorage.SCHEMA_ID.get());
        Connection connection = super.getConnection();
        connection
                .createStatement()
                .execute("SET search_path to " + Optional.ofNullable(TenantThreadLocalStorage.SCHEMA_ID.get()).orElse("public"));
        return connection;
    }

}
