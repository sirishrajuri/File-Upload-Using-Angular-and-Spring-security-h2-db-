package com.bezkoder.springjwt.config.mongodb;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.bezkoder.springjwt.repository",
        mongoTemplateRef = "noSqlMongoTemplate"
)
public class MongoDB {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017/");
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "UploadFiles_DB");
    }

    @Bean(name = "noSqlMongoTemplate")
    public MongoTemplate noSqlMongoTemplate(MongoDatabaseFactory mongoDbFactory, MongoMappingContext context) {
        return new MongoTemplate(mongoDbFactory);
    }
}
