package org.unidue.ub.unidue.almaregister.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"org.unidue.ub.unidue.almaregister.model.his"})
@EnableJpaRepositories(basePackages = {"org.unidue.ub.unidue.almaregister.repository"})
@EnableTransactionManagement
public class DatabaseConfiguration {

}
