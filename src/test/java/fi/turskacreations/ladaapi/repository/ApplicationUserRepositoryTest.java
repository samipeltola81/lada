package fi.turskacreations.ladaapi.repository;


import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.user.ApplicationUserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ApplicationUserRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Before
    public void setUp(){
        if(!mongoTemplate.collectionExists(ApplicationUser.class)){
            mongoTemplate.createCollection(ApplicationUser.class);
        }
    }

    @After
    public void tearDown(){
        if(mongoTemplate.collectionExists(ApplicationUser.class)){
            mongoTemplate.dropCollection(ApplicationUser.class);
        }
    }

    @Test
    public void find_by_username_OK(){

        String username = "Tester1";
        String activationKey = "123";
        ApplicationUser expectedUser = ApplicationUser.builder().username(username).activationKey(activationKey).build();

        mongoTemplate.save(expectedUser);

        Optional<ApplicationUser> applicationUser = applicationUserRepository.findByUsername(username);

        assertNotNull(applicationUser);
        assertEquals(username, applicationUser.get().getUsername());

    }

    @Test
    public void find_by_activationKey_OK(){

        String username = "Tester1";
        String activationKey = "123";
        ApplicationUser expectedUser = ApplicationUser.builder().username(username).activationKey(activationKey).build();

        mongoTemplate.save(expectedUser);

        Optional<ApplicationUser> applicationUser = applicationUserRepository.findByUsername(username);

        assertNotNull(applicationUser);
        assertEquals(activationKey, applicationUser.get().getActivationKey());

    }

}
