package fi.turskacreations.ladaapi.db;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.*;

@Configuration
@EnableMongoRepositories(basePackages = "fi.turskacreations.ladaapi")
@ComponentScan(basePackageClasses = fi.turskacreations.ladaapi.package_info.class)
@EnableMongoAuditing
public class MongoConfigurationCommon extends AbstractMongoConfiguration{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.data.mongodb.host}")
    String host = "localhost";
    @Value("${spring.data.mongodb.port}")
    Integer port = 27017;


    @Override
    public MongoClient mongoClient() {
        MongoClient mongoClient = new MongoClient(host, port);
        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return "lada";
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singletonList("fi.turskacreations.ladaapi");
    }

    @Bean
    public Mongobee mongobee() throws Exception {
        String uri =  host +  ":" +  port.toString();
        Mongobee mongobee = new Mongobee("mongodb://" + uri);
        mongobee.setDbName(this.getDatabaseName());         // host must be set if not set in URI
        mongobee.setChangeLogsScanPackage(
                "fi.turskacreations.ladaapi.db"); // the package to be scanned for changesets
        // Mongobee constructor does not work correctly with spring-data-mongodb-2.0.0+
        mongobee.setMongoTemplate(mongoTemplate());
        return mongobee;
    }

}
