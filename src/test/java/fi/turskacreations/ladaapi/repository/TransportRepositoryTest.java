package fi.turskacreations.ladaapi.repository;


import fi.turskacreations.ladaapi.domain.Sequence;
import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.transport.TransportRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fi.turskacreations.ladaapi.constants.Sequences.TRANSPORTS_SEQUENCE_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//@ActiveProfiles("IT-test")
@RunWith(SpringRunner.class)
@DataMongoTest
public class TransportRepositoryTest {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TransportRepository transportRepository;

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
    public void getTransport_and_return_OK(){

        Long expectedTransportId = new Long(1);

        Transport expectedTransport = Transport.builder()
                .transportId(expectedTransportId)
                .build();

        mongoTemplate.save(expectedTransport);

        Transport transport = transportRepository.findTransportByTransportId(expectedTransportId).get();
        assertNotNull(transport);
        assertEquals(expectedTransportId, transport.getTransportId());
    }

    @Test
    public void get_all_transports_and_return_OK(){

        Long expectedTransportId1 = new Long(1);
        Long expectedTransportId2 = new Long(2);

        List<Transport> expectedTransportList = Arrays.asList(
                Transport.builder()
                    .transportId(expectedTransportId1)
                    .registeredTrackingCodes(new ArrayList<>())
                    .build(),
                Transport.builder()
                    .transportId(expectedTransportId2)
                    .registeredTrackingCodes(new ArrayList<>())
                    .build()
        );

        mongoTemplate.insertAll(expectedTransportList);

        List<Transport> transportList = transportRepository.findAll();
        assertNotNull(transportList);
        assertEquals(expectedTransportList, transportList);
    }

    @Test
    public void saveTransport_and_return_transportId(){

        Transport expectedTransport = Transport.builder()
                .build();
        // Init sequence
        Sequence sequence = Sequence.builder().sequenceKey(TRANSPORTS_SEQUENCE_KEY).seq(0).build();
        mongoTemplate.save(sequence, "sequence");

        Long expectedTransportId = transportRepository.saveTransport(expectedTransport);

        Transport transport = transportRepository.findTransportByTransportId(expectedTransportId).get();

        assertNotNull(transport);
        assertEquals(expectedTransportId, transport.getTransportId());

        logger.info("Created a new transport with transport sequenceKey: {}", transport.getTransportId());

    }

    @Test
    public void update_existing_transport_and_verify_transportId_does_not_increase(){

        Transport expectedTransport = Transport.builder()
                .build();
        // Init sequence
        Sequence sequence = Sequence.builder().sequenceKey(TRANSPORTS_SEQUENCE_KEY).seq(0).build();
        mongoTemplate.save(sequence, "sequence");

        Long expectedTransportId = transportRepository.saveTransport(expectedTransport);
        Transport transport = transportRepository.findTransportByTransportId(expectedTransportId).get();

        assertNotNull(transport);
        assertEquals(expectedTransportId, transport.getTransportId());


        // Update the existing transport and make sure the transportId does not increase
        Long actualUpdatedTransportId = transportRepository.saveTransport(expectedTransport);

        assertEquals(expectedTransportId, actualUpdatedTransportId);

    }




}
