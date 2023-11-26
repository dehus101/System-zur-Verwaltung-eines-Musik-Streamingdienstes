package de.hhu.cs.dbs.propra.persistence;

import static org.springframework.data.repository.query.QueryLookupStrategy.Key.USE_DECLARED_QUERY;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(queryLookupStrategy = USE_DECLARED_QUERY)
public class PersistenceConfiguration {}
