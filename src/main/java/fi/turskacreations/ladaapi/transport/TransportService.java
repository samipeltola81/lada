package fi.turskacreations.ladaapi.transport;

import fi.turskacreations.ladaapi.domain.Event;
import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.dto.TrackingCodeList;

import java.util.List;


public interface TransportService {


    Transport getTransport(Long trackingCode);

    List<Transport> getAllTransports();

    Long saveTransport(Transport transport);

    Transport registerTransportationUnits(Long transportId, TrackingCodeList trackingCodeList);

    void registerEventToTransportationUnit(Long transportId, String trackingCode, Event event);

    void registerEventToTransportationUnit(Long transportId, TrackingCodeList trackingCodeList, Event event);

}
