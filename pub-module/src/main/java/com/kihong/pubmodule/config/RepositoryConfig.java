package com.kihong.pubmodule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.kihong.pubmodule.adapter.out.persistence.mongo")
@EnableJpaRepositories(basePackages = "com.kihong.pubmodule.adapter.out.persistence.jpa")
public class RepositoryConfig {
}
