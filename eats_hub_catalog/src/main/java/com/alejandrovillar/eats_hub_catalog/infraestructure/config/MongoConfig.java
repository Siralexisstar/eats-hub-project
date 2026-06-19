package com.alejandrovillar.eats_hub_catalog.infraestructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories("com.alejandrovillar.eats_hub_catalog")
@PropertySource(value = "classpath:mongo-connection.properties")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    //Configuring the values importes from mongo-connection.properties
    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private Integer port;

    @Value("${mongodb.database}")
    private String database;

    @Value("${mongodb.username}")
    private String username;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.authentication-database}")
    private String authSource;

    @Value("${mongodb.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${mongodb.minPoolSize}")
    private Integer minPoolSize;

    @Value("${mongodb.maxConnectionLifeTime}")
    private Long maxConnectionLifeTime;

    //Returning the database name (Obligatory)
    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    //We build the connection in 3 steps
    @Bean
    @Primary
    @Override
    public MongoClient reactiveMongoClient() {
        //Client side --> to build the URL
        final ConnectionString connectionString = new ConnectionString(
                String.format("mongodb://%s:%d/%s", host, port, database));


        //Building auth credentials
        final MongoCredential mongoCredential = MongoCredential.createCredential(
                username, authSource, password.toCharArray());

        //Final configuration
        MongoClientSettings mongoClienSettings = MongoClientSettings
                .builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .credential(mongoCredential)
                .applyToConnectionPoolSettings(poolBuilder ->
                        poolBuilder.maxSize(maxPoolSize).minSize(minPoolSize)
                                .maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MILLISECONDS)
                ).build();

        return MongoClients.create(mongoClienSettings);
    }

    //Exposes the bean to inyect in other components
    //Like a Autowired imyecting the mongoClient before created
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        return new ReactiveMongoTemplate(mongoClient, getDatabaseName());
    }

}

