package fi.turskacreations.ladaapi.repository;


import fi.turskacreations.ladaapi.domain.TransportationUnit;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

//@ActiveProfiles("IT-test")
@RunWith(SpringRunner.class)
@DataMongoTest
public class TransportationUnitRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TransportationUnitRepository transportationUnitRepository;

    @Before
    public void setUp(){
        if(!mongoTemplate.collectionExists(TransportationUnit.class)){
            mongoTemplate.createCollection(TransportationUnit.class);
        }
    }

    @After
    public void tearDown(){
        if(mongoTemplate.collectionExists(TransportationUnit.class)){
            mongoTemplate.dropCollection(TransportationUnit.class);
        }
    }

    @Test
    public void checkMongoTemplate() {
        String collectionName = "transportation_units";
        assertNotNull(mongoTemplate);
        assertTrue(mongoTemplate.collectionExists(collectionName));
    }

    @Test
    public void find_by_trackingCode_and_return_OK(){

        String expectedTrackingCode = UUID.randomUUID().toString();
        TransportationUnit expectedTransportationUnit = TransportationUnit.builder().trackingCode(expectedTrackingCode).build();

        mongoTemplate.save(expectedTransportationUnit);

        Optional<TransportationUnit> transportationUnit = transportationUnitRepository.findTransportationUnitByTrackingCode(expectedTrackingCode);

        assertNotNull(transportationUnit);
        assertEquals(expectedTransportationUnit.getTrackingCode(), transportationUnit.get().getTrackingCode());

    }

    @Test
    public void find_all_transportation_units_and_return_OK(){

        String expectedTrackingCode1 = UUID.randomUUID().toString();
        String expectedTrackingCode2 = UUID.randomUUID().toString();
        List<TransportationUnit> expectedTransportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(expectedTrackingCode1).events(new ArrayList<>()).build(),
                TransportationUnit.builder().trackingCode(expectedTrackingCode2).events(new ArrayList<>()).build()
        );

        mongoTemplate.insertAll(expectedTransportationUnitList);

        List<TransportationUnit> transportationUnitList = transportationUnitRepository.findAll();

        assertNotNull(transportationUnitList);
        assertEquals(expectedTransportationUnitList, transportationUnitList);

    }



    @Test
    public void save_with_trackingCode_and_return_OK(){

        UUID expectedTrackingCode = UUID.randomUUID();
        TransportationUnit expectedTransportationUnit = TransportationUnit.builder().trackingCode(expectedTrackingCode.toString()).build();

        transportationUnitRepository.save(expectedTransportationUnit);

        Optional<TransportationUnit> foundTransportationUnit = transportationUnitRepository.findTransportationUnitByTrackingCode(expectedTrackingCode.toString());

        List<TransportationUnit> transportationUnitList = mongoTemplate.find(Query.query(Criteria.where("trackingCode").is(expectedTrackingCode.toString())),TransportationUnit.class);

        transportationUnitList.forEach(t -> logger.debug("Found transportation units: {}", t));

        assertNotNull(foundTransportationUnit);
        assertEquals(expectedTransportationUnit.getTrackingCode(), foundTransportationUnit.get().getTrackingCode());

    }

}
