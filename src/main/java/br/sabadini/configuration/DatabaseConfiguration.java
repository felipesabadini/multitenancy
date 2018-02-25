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
        targetDataSources.put("tenancytwo", DatabaseConfiguration.createDatasourceByName("tenancyone"));
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

    public static void updateDatabaseByName(String name) {
        try {
            Connection connection = createDatasourceByName(name).getConnection();
            JdbcConnection jdbcConnection = new JdbcConnection(connection);

            Database correctDatabaseImplementation = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(jdbcConnection);

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
                props.setProperty("dataSource.user", "postgres");
                props.setProperty("dataSource.password", "senha");
                props.setProperty("dataSource.databaseName", "tenancytwo");
                break;
            case "tenancythree":
                props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
                props.setProperty("dataSource.user", "postgres");
                props.setProperty("dataSource.password", "senha");
                props.setProperty("dataSource.databaseName", "tenancythree");
                break;
            default:
                props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
                props.setProperty("dataSource.user", "postgres");
                props.setProperty("dataSource.password", "senha");
                props.setProperty("dataSource.databaseName", "tenancyone");
                break;
        }
        return props;
    }

}
