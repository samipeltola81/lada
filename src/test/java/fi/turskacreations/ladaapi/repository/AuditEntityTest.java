package fi.turskacreations.ladaapi.repository;


import fi.turskacreations.ladaapi.domain.Sequence;
import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.security.SecurityConfiguration;
import fi.turskacreations.ladaapi.security.WebSecurityConfiguration;
import fi.turskacreations.ladaapi.transport.TransportRepository;
import fi.turskacreations.ladaapi.transport.TransportService;
import fi.turskacreations.ladaapi.transport.TransportServiceImpl;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitService;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitServiceImpl;
import fi.turskacreations.ladaapi.user.ApplicationUserDetailsServiceImpl;
import fi.turskacreations.ladaapi.user.ApplicationUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
@ImportAutoConfiguration(value = {WebSecurityConfiguration.class, SecurityConfiguration.class})
public class AuditEntityTest {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TransportRepository transportRepository;



    @TestConfiguration
    @EnableMongoAuditing
    static class AuditEntityTestContextConfiguration{

        @MockBean
        ApplicationUserRepository applicationUserRepository;

        @Bean
        @Qualifier("applicationUserDetailsServiceImpl")
        public UserDetailsService userDetailsService(){
            return new ApplicationUserDetailsServiceImpl(applicationUserRepository);
        }


    }

    @Before
    public void setUp(){
        if(!mongoTemplate.collectionExists(Sequence.class)){
            mongoTemplate.createCollection(Sequence.class);
        }
        if(!mongoTemplate.collectionExists(Transport.class)){
            mongoTemplate.createCollection(Transport.class);
        }
    }

    @After
    public void tearDown(){
        if(mongoTemplate.collectionExists(Transport.class)){
            mongoTemplate.dropCollection(Transport.class);
        }
        if(mongoTemplate.collectionExists(Sequence.class)){
            mongoTemplate.dropCollection(Sequence.class);
        }
    }


    @Test
    @WithMockUser(value = "admin")
    public void insert_new_document_and_have_audit_info(){

        Long expectedTransportId = new Long(1);

        Transport expectedTransport = Transport.builder()
                .transportId(expectedTransportId)
                .build();

        transportRepository.save(expectedTransport);

        Transport transport = transportRepository.findTransportByTransportId(expectedTransportId).get();

        assertNotNull(transport);
        assertEquals(expectedTransportId, transport.getTransportId());
        assertNotNull(transport.getCreatedDate());

    }

    @Test
    @WithMockUser(value = "admin")
    public void update_existing_document_and_createdDate_does_not_change(){

        Long expectedTransportId = new Long(1);

        Transport expectedTransport = Transport.builder()
                .transportId(expectedTransportId)
                .build();

        transportRepository.save(expectedTransport);
        Transport transport = transportRepository.findTransportByTransportId(expectedTransportId).get();

        Instant expectedCreatedDate = transport.getCreatedDate();

        assertNotNull(transport);
        assertEquals(expectedTransportId, transport.getTransportId());
        assertNotNull(transport.getCreatedDate());

        transportRepository.save(expectedTransport);
        transport = transportRepository.findTransportByTransportId(expectedTransportId).get();

        assertEquals(expectedCreatedDate, transport.getCreatedDate());

    }


}
