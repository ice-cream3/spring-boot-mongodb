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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.nio.file.Paths;

/**
 * @ClassName: MongoConfig
 * @Description: mongo数据连接
 * @Author: dream
 * @Date: 2024/3/29 17:15
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.example.demo")
public class MongodbConfig {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    /*@Value("${mongodb.atlas}")
    private boolean atlas;

    @Value("${truststore.path}")
    private String trustStorePath;

    @Value("${truststore.pwd}")
    private String trustStorePwd;*/

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
                .applyConnectionString(connectionString)
                /* ssl安全认证
                .applyToSslSettings(builder -> {
                    if (!atlas) {
                        // Use SSLContext if a trustStore has been provided
                        if (!trustStorePath.isEmpty()) {
                            SSLFactory sslFactory = SSLFactory.builder()
                                    .withTrustMaterial(Paths.get(trustStorePath), trustStorePwd.toCharArray())
                                    .build();
                            SSLContext sslContext = sslFactory.getSslContext();
                            builder.context(sslContext);
                            builder.invalidHostNameAllowed(true);
                        }
                    }
                    builder.enabled(true);
                })*/.build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoClient mongoClient = mongoClient();
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
        return new MongoTemplate(factory);
        // return new MongoTemplate(mongoClient(), databaseName);
    }

}
