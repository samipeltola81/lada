package fi.turskacreations.ladaapi.transport;

import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.domain.Event;
import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.domain.TransportationUnit;
import fi.turskacreations.ladaapi.exceptions.ResourceNotFoundException;
import fi.turskacreations.ladaapi.dto.TrackingCodeList;
import fi.turskacreations.ladaapi.exceptions.TransportationUnitAlreadyRegistered;
import fi.turskacreations.ladaapi.exceptions.UnAuthorizedActionException;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransportServiceImpl implements TransportService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TransportRepository transportRepository;

    @Autowired
    TransportationUnitService transportationUnitService;

    @Override
    public Transport getTransport(Long transportId) {
        return transportRepository.findTransportByTransportId(transportId).orElseThrow(() -> new ResourceNotFoundException(String.format("Transport not found with: %d", transportId)));
    }

    @Override
    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    @Override
    public Long saveTransport(Transport transport) {
        return transportRepository.saveTransport(transport);
    }

    @Override
    public Transport registerTransportationUnits(Long transportId, TrackingCodeList trackingCodeList) {

        Transport transport = this.getTransport(transportId);

        List<TransportationUnit> transportationUnitList = trackingCodeList.getTrackingCodes().stream()
                .map(t -> transportationUnitService.getTransportationUnit(t))
                .collect(Collectors.toList())
                ;

        if(transport.getRegisteredTrackingCodes() != null){
            transportationUnitList.forEach( tu -> {
                if(transport.getRegisteredTrackingCodes().contains(tu.getTrackingCode())){
                    throw new TransportationUnitAlreadyRegistered(String.format("Transportation unit %s already registered to transport %d", tu.getTrackingCode(), transport.getTransportId()));
                }
            } );
        } else {
            transport.setRegisteredTrackingCodes(new ArrayList<>());
        }

        transport.getRegisteredTrackingCodes().addAll(trackingCodeList.getTrackingCodes());

        transportRepository.saveTransport(transport);

        return transport;
    }

    @Override
    public void registerEventToTransportationUnit(Long transportId, String trackingCode, Event event) {
        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode)).build();
        this.handleEventRegistration(transportId, trackingCodeList, event);
    }

    @Override
    public void registerEventToTransportationUnit(Long transportId, TrackingCodeList trackingCodeList, Event event) {
        this.handleEventRegistration(transportId, trackingCodeList, event);
    }

    public void handleEventRegistration(Long transportId, TrackingCodeList trackingCodeList, Event event ){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        logger.info("User {} registering event: {} to trackingCodeList: {}", username, event, trackingCodeList);

        Transport transport = this.getTransport(transportId);

        // Authorize
        Objects.requireNonNull(transport.getDriver(), String.format("transport %d does not have a driver", transportId));
        if(!transport.getDriver().equals(username))
            throw new UnAuthorizedActionException(String.format("User %s is not the driver of transport: %d", username, transportId));

        Objects.requireNonNull(transport.getRegisteredTrackingCodes(), String.format("Transport %d does not have any registered transportation units", transportId));


        List<TransportationUnit> transportationUnitList = transport.getRegisteredTrackingCodes().stream()
                .filter(tc -> trackingCodeList.getTrackingCodes().contains(tc))
                .map(tu -> transportationUnitService.getTransportationUnit(tu))
                .collect(Collectors.toList())
                ;
        transportationUnitList.forEach(tu -> {
                    if(tu.getEvents() == null) tu.setEvents(new ArrayList<>());
                    tu.getEvents().add(enrichEvent(event, username));
                    transportationUnitService.save(tu);
                }
        );
    }

    private Event enrichEvent(Event event, String username){

        event.setUser(username);
        event.setCreatedDate(Instant.now());

        return event;
    }

}
