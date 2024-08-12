package com.qsdl.demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @ClassName: MongoConfig
 * @Description: mongo数据连接
 * @Author: dream
 * @Date: 2024/3/29 17:15
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.example.demo")
public class MongodbConfig extends AbstractMongoClientConfiguration{

    @Value("${mongodb.uri}")
    private String uri;

    @Value("${mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        /*CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(codecRegistry)
                .build());*/
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoClient mongoClient = mongoClient();
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
        return new MongoTemplate(factory);
        // return new MongoTemplate(mongoClient(), databaseName);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public DbRefResolver dbRefResolver(MongoDatabaseFactory dbFactory) {
        return new DefaultDbRefResolver(dbFactory);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory databaseFactory, MongoCustomConversions customConversions, MongoMappingContext mappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(databaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(customConversions);
        converter.setCodecRegistryProvider(databaseFactory);
        return converter;
    }
}
