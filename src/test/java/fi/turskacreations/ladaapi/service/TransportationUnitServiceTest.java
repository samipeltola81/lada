package fi.turskacreations.ladaapi.service;

import fi.turskacreations.ladaapi.domain.TransportationUnit;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitRepository;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitService;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class TransportationUnitServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @MockBean
    TransportationUnitRepository transportationUnitRepository;

    @Autowired
    TransportationUnitService transportationUnitService;

    @TestConfiguration
    static class TransportationUnitServiceTestContextConfiguration{

        @Bean
        public TransportationUnitService transportationUnitService(){
            return new TransportationUnitServiceImpl();
        }



    }

    @Test
    public void save_transportationUnit_without_trackingCode_and_generate_trackingCode(){

        ArgumentCaptor<TransportationUnit> argumentCaptor = ArgumentCaptor.forClass(TransportationUnit.class);

        TransportationUnit expectedTransportationUnit = TransportationUnit.builder().build();

        given(transportationUnitRepository.save(argumentCaptor.capture())).willAnswer(p -> p.getArgument(0));

        TransportationUnit transportationUnit = transportationUnitService.save(expectedTransportationUnit);

        logger.info("Saved transportation unit: {}", transportationUnit);

        assertNotNull(argumentCaptor.getValue().getTrackingCode());
        assertNotNull(transportationUnit.getTrackingCode());

    }



}
