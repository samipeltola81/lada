package fi.turskacreations.ladaapi.transportation_unit;

import fi.turskacreations.ladaapi.domain.TransportationUnit;

import java.util.List;
import java.util.UUID;


public interface TransportationUnitService {

    TransportationUnit getTransportationUnit(String trackingCode);

    List<TransportationUnit> getAllTransportationUnits();

    TransportationUnit save(TransportationUnit transportationUnit);
}
