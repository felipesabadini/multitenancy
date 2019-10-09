package br.sabadini.configuration;

import com.zaxxer.hikari.HikariConfig;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();
        Map<Object,Object> targetDataSources = new HashMap<>();

        targetDataSources.put("tenancyone", DatabaseConfiguration.createDatasourceByName("tenancyone"));
        targetDataSources.put("tenancytwo", DatabaseConfiguration.createDatasourceByName("tenancytwo"));
        dataSource.setTargetDataSources(targetDataSources);

        dataSource.afterPropertiesSet();

        return dataSource;
    }

    protected static SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(Boolean.TRUE);

        return liquibase;
    }

    public static void updateDatabaseByName(String name, String schema) {
        try {
            Connection connection = createDatasourceByName(name).getConnection();
            JdbcConnection jdbcConnection = new JdbcConnection(connection);

            Database correctDatabaseImplementation = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(jdbcConnection);
            correctDatabaseImplementation.setLiquibaseSchemaName(schema);
            Liquibase liquibase = new Liquibase("liquibase/changelog-master.xml", new ClassLoaderResourceAccessor(), correctDatabaseImplementation);

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException e) {

        }
    }

    public static DataSource createDatasourceByName(String name) {
        HikariConfig config = new HikariConfig(DatabaseConfiguration.getDatasourceByName(name));
        return new TenantHikariDataSource(config);
    }

    public static Properties getDatasourceByName(String name) {
        Properties props = new Properties();
        switch (name) {
            case "tenancytwo":
                props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
                props.setProperty("dataSource.user", "mobiage");
                props.setProperty("dataSource.password", "77726F981F568BA3348E3BD5F65D6D9F8E618C7C21FBEE2AAEE8656A4E68E437");
                props.setProperty("dataSource.url", "jdbc:postgresql://127.0.0.1:7779/tenancytwo");
                break;
            case "tenancythree":
                props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
                props.setProperty("dataSource.user", "mobiage");
                props.setProperty("dataSource.password", "77726F981F568BA3348E3BD5F65D6D9F8E618C7C21FBEE2AAEE8656A4E68E437");
                props.setProperty("dataSource.url", "jdbc:postgresql://127.0.0.1:7779/tenancythree");
                break;
            default:
                props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
                props.setProperty("dataSource.user", "mobiage");
                props.setProperty("dataSource.password", "77726F981F568BA3348E3BD5F65D6D9F8E618C7C21FBEE2AAEE8656A4E68E437");
                props.setProperty("dataSource.url", "jdbc:postgresql://127.0.0.1:7779/tenancyone");
                break;
        }
        return props;
    }

}
