package fi.turskacreations.ladaapi.service;


import fi.turskacreations.ladaapi.domain.*;
import fi.turskacreations.ladaapi.dto.TrackingCodeList;
import fi.turskacreations.ladaapi.exceptions.ResourceNotFoundException;
import fi.turskacreations.ladaapi.exceptions.TransportationUnitAlreadyRegistered;
import fi.turskacreations.ladaapi.exceptions.UnAuthorizedActionException;
import fi.turskacreations.ladaapi.transport.TransportRepository;
import fi.turskacreations.ladaapi.transport.TransportService;
import fi.turskacreations.ladaapi.transport.TransportServiceImpl;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TransportServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @MockBean
    TransportRepository transportRepository;
    @MockBean
    TransportationUnitRepository transportationUnitRepository;

    @Autowired
    TransportService transportService;

    @TestConfiguration
    static class TransportationServiceTestContextConfiguration{

        @Bean
        public TransportService transportService(){
            return new TransportServiceImpl();
        }

        @Bean
        public TransportationUnitService transportationUnitService(){
            return new TransportationUnitServiceImpl();
        }

    }

    @Test
    public void register_one_transportation_unit(){

        String trackingCode = "trackingCode1";
        TransportationUnit transportationUnit = TransportationUnit.builder().trackingCode(trackingCode).build();
        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode)).build();
        Long transportId = Long.valueOf(1);

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Transport> saveArgumentCaptor = ArgumentCaptor.forClass(Transport.class);

        Transport expectedTransport = Transport.builder().transportId(transportId).build();


        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode)).willReturn(Optional.of(transportationUnit));
        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));
        given(transportRepository.saveTransport(saveArgumentCaptor.capture())).willReturn(expectedTransport.getTransportId());

        Transport resultsTransport = transportService.registerTransportationUnits(transportId, trackingCodeList);


        assertEquals(expectedTransport, resultsTransport);
        assertEquals(saveArgumentCaptor.getValue().getRegisteredTrackingCodes().get(0), transportationUnit.getTrackingCode());

    }

    @Test
    public void register_multiple_transportation_units(){

        String trackingCode1 = "trackingCode1";
        String trackingCode2 = "trackingCode2";
        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build(),
                TransportationUnit.builder().trackingCode(trackingCode2).build()
        );
        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode1, trackingCode2)).build();
        Long transportId = Long.valueOf(1);

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Transport> saveArgumentCaptor = ArgumentCaptor.forClass(Transport.class);

        Transport expectedTransport = Transport.builder().transportId(transportId).build();

        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode1)).willReturn(Optional.of(transportationUnitList.get(0)));
        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode2)).willReturn(Optional.of(transportationUnitList.get(1)));
        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));
        given(transportRepository.saveTransport(saveArgumentCaptor.capture())).willReturn(expectedTransport.getTransportId());

        Transport resultsTransport = transportService.registerTransportationUnits(transportId, trackingCodeList);


        assertEquals(expectedTransport, resultsTransport);
        assertEquals(trackingCodeList.getTrackingCodes(), saveArgumentCaptor.getValue().getRegisteredTrackingCodes() );

    }

    // Test missing transportation unit
    @Test(expected = ResourceNotFoundException.class)
    public void register_transportation_unit_that_does_not_exist(){

        String trackingCode1 = "trackingCode1";
        String trackingCode2 = "trackingCode2";
        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build(),
                TransportationUnit.builder().trackingCode(trackingCode2).build()
        );
        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode1, trackingCode2)).build();
        Long transportId = Long.valueOf(1);

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        //ArgumentCaptor<Transport> saveArgumentCaptor = ArgumentCaptor.forClass(Transport.class);


        Transport expectedTransport = Transport.builder().transportId(transportId).build();

        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode1)).willReturn(Optional.of(transportationUnitList.get(0)));
        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode2)).willThrow(new ResourceNotFoundException("Not found"));
        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));

        Transport resultsTransport = transportService.registerTransportationUnits(transportId, trackingCodeList);

    }


    // Test already registered
    @Test(expected = TransportationUnitAlreadyRegistered.class)
    public void register_transportation_unit_that_is_already_registered(){

        String trackingCode1 = "trackingCode1";
        String trackingCode2 = "trackingCode2";
        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build(),
                TransportationUnit.builder().trackingCode(trackingCode2).build()
        );
        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode1, trackingCode2)).build();
        Long transportId = Long.valueOf(1);

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        Transport expectedTransport = Transport.builder().transportId(transportId).registeredTrackingCodes(Arrays.asList(trackingCode2)).build();

        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));
        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode1)).willReturn(Optional.of(transportationUnitList.get(0)));
        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode2)).willReturn(Optional.of(transportationUnitList.get(1)));


        Transport resultsTransport = transportService.registerTransportationUnits(transportId, trackingCodeList);

    }

    // test missing transport
    @Test(expected = ResourceNotFoundException.class)
    public void register_transportation_unit_to_tranport_that_does_not_exist(){

        String trackingCode1 = "trackingCode1";
        String trackingCode2 = "trackingCode2";
        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build(),
                TransportationUnit.builder().trackingCode(trackingCode2).build()
        );

        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode1, trackingCode2)).build();
        Long transportId = Long.valueOf(1);

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willThrow(new ResourceNotFoundException("Not Found"));

        Transport resultsTransport = transportService.registerTransportationUnits(transportId, trackingCodeList);

    }


    @Test
    @WithMockUser("admin")
    public void register_event_to_transportation_unit(){

        String trackingCode1 = "trackingCode1";
        Long transportId = Long.valueOf(1);
        HashMap<String,String> additionalInfo =  new HashMap<String, String>(){{put( "Event","Loaded to transport: " + transportId);}};
        Event event = Event.builder()
                .eventType(EventType.REGISTERED)
                .details("Freeform details to define the event")
                .additionalInfo(additionalInfo)
                .build();

        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build()
        );

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TransportationUnit> saveArgumentCaptor = ArgumentCaptor.forClass(TransportationUnit.class);

        Transport expectedTransport = Transport.builder()
                .transportId(transportId)
                .registeredTrackingCodes(Arrays.asList(trackingCode1))
                .driver("admin")
                .build();

        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));
        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode1)).willReturn(Optional.of(transportationUnitList.get(0)));
        given(transportationUnitRepository.save(saveArgumentCaptor.capture())).willReturn(transportationUnitList.get(0));

        transportService.registerEventToTransportationUnit(transportId,trackingCode1, event);

        assertEquals(event, saveArgumentCaptor.getValue().getEvents().get(0) );

    }

    @Test(expected = NullPointerException.class)
    @WithMockUser("admin")
    public void register_event_to_transportation_unit_without_TUs(){

        String trackingCode1 = "trackingCode1";
        Long transportId = Long.valueOf(1);
        HashMap<String,String> additionalInfo =  new HashMap<String, String>(){{put( "Event","Loaded to transport: " + transportId);}};
        Event event = Event.builder()
                .eventType(EventType.REGISTERED)
                .details("Freeform details to define the event")
                .additionalInfo(additionalInfo)
                .build();

        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build()
        );

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Transport expectedTransport = Transport.builder().transportId(transportId).build();

        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));
        given(transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode1)).willReturn(Optional.of(transportationUnitList.get(0)));


        transportService.registerEventToTransportationUnit(transportId,trackingCode1, event);


    }

    @Test(expected = UnAuthorizedActionException.class)
    @WithMockUser("not_admin")
    public void try_to_register_event_with_incorrect_username(){

        String trackingCode1 = "trackingCode1";
        Long transportId = Long.valueOf(1);
        HashMap<String,String> additionalInfo =  new HashMap<String, String>(){{put( "Event","Loaded to transport: " + transportId);}};
        Event event = Event.builder()
                .eventType(EventType.REGISTERED)
                .details("Freeform details to define the event")
                .additionalInfo(additionalInfo)
                .build();

        List<TransportationUnit> transportationUnitList = Arrays.asList(
                TransportationUnit.builder().trackingCode(trackingCode1).build()
        );

        ArgumentCaptor<Long> findArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Transport expectedTransport = Transport.builder()
                .transportId(transportId)
                .registeredTrackingCodes(Arrays.asList(trackingCode1))
                .driver("admin")
                .build();

        given(transportRepository.findTransportByTransportId(findArgumentCaptor.capture())).willReturn(Optional.of(expectedTransport));

        transportService.registerEventToTransportationUnit(transportId,trackingCode1, event);


    }




}
