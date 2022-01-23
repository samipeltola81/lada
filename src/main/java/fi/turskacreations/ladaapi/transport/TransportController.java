package fi.turskacreations.ladaapi.transport;

import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.domain.Event;
import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.dto.TrackingCodeList;
import fi.turskacreations.ladaapi.dto.TransportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fi.turskacreations.ladaapi.constants.Paths.API;
import static fi.turskacreations.ladaapi.constants.Paths.TRANSPORT;

@RestController
@RequestMapping(API + TRANSPORT)
public class TransportController {

    @Autowired
    TransportService transportService;


    @GetMapping("/{transportId}")
    public Transport getTransport(@PathVariable Long transportId){

        return transportService.getTransport(transportId);

    }

    @GetMapping("")
    public List<Transport> getTransport(){

        return transportService.getAllTransports();

    }

    @PostMapping
    public TransportResponse createTransport(@RequestBody Transport transport, Authentication authentication){
        String driver = authentication.getPrincipal().toString();
        transport.setDriver(driver);
        Long transportId = transportService.saveTransport(transport);
        return TransportResponse.builder().transportId(transportId).build();

    }

    @PostMapping("/{transportId}/transportation-units")
    public Transport registerTransportationUnits(@RequestBody TrackingCodeList trackingCodeList, @PathVariable Long transportId){

        Transport transport = transportService.registerTransportationUnits(transportId, trackingCodeList);

        return transport;
    }

    @PostMapping("/{transportId}/transportation-units/{trackingCode}/events")
    public void registerEventToTransportationUnit(@RequestBody Event event, @PathVariable Long transportId, @PathVariable String trackingCode){

        transportService.registerEventToTransportationUnit(transportId, trackingCode, event);

    }



}
