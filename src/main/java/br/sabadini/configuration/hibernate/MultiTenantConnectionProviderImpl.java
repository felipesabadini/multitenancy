package br.sabadini.configuration.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Deprecated
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    @SuppressWarnings("all")
    public Connection getConnection(String schema) throws SQLException {
        final Connection connection = this.getAnyConnection();
        try {
            connection.createStatement().execute("SET search_path to " + schema);
        } catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + schema + "]", e);
        }
        return connection;
    }

    @Override
    @SuppressWarnings("all")
    public void releaseConnection(String schema, Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("SET search_path to public");
        } catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + schema + "]",
                    e);
        }

        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
