package de.hhu.cs.dbs.propra.persistence.sql.sqlite;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.util.Map;
import java.util.Properties;

@Configuration
public class SqliteConfiguration {

    @Bean
    @Conditional(SqliteCondition.class)
    SQLiteDataSource dataSource(DataSourceProperties dataSourceProperties) {
        Properties properties = new Properties();
        properties.put("auto_vacuum", "full");
        SQLiteConfig config = new SQLiteConfig(properties);
        config.setEncoding(SQLiteConfig.Encoding.UTF8);
        config.enforceForeignKeys(true);
        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        dataSource.setUrl(dataSourceProperties.getUrl());
        return dataSource;
    }

    public static class SqliteCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Environment env = context.getEnvironment();
            for (PropertySource<?> propertySource :
                    ((AbstractEnvironment) env).getPropertySources()) {
                if (propertySource instanceof MapPropertySource) {
                    for (Map.Entry<String, Object> entry :
                            ((MapPropertySource) propertySource).getSource().entrySet()) {
                        if (entry.getKey().startsWith("spring.datasource.url")
                                && entry.getValue().toString().startsWith("jdbc:sqlite:")) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
