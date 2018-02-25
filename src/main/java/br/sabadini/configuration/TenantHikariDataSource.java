package br.sabadini.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class TenantHikariDataSource extends HikariDataSource {

    public TenantHikariDataSource() {
        super();
    }

    public TenantHikariDataSource(HikariConfig configuration) {
        super(configuration);
    }

    @Override
    @SuppressWarnings("all")
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        connection
                .createStatement()
                .execute("SET search_path to " + Optional.ofNullable(TenantThreadLocalStorage.SCHEMA_ID.get()).orElse("public"));
        return connection;
    }

}
