package com.covid.nepal.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.SocketSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database:Covid}")
    private String dbName;
    @Value("${spring.data.mongodb.connString:mongodb+srv://yudeep:yudeep@yudeep-v9isw.mongodb.net/test?retryWrites=true&w=majority}")
    private String connectionString;
    ConnectionString connString = new ConnectionString("mongodb+srv://yudeep:yudeep1234@cluster0-ecdnb.mongodb.net/test?retryWrites=true&w=majority");
    @Override
    public MongoClient mongoClient() {

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyToConnectionPoolSettings(builder ->
                        builder.maxConnectionIdleTime(5000, TimeUnit.MILLISECONDS))
                .applicationName("Covid")
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return  mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }
}
