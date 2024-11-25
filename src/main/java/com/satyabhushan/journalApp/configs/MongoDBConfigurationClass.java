package com.satyabhushan.journalApp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
public class MongoDBConfigurationClass {

    /*
    No qualifying bean of type 'org.springframework.transaction.TransactionManager' available
     */
    @Bean
    public PlatformTransactionManager getTransactionManagerBean(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
    }
}
