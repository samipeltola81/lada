package fi.turskacreations.ladaapi.transportation_unit;

import fi.turskacreations.ladaapi.domain.TransportationUnit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportationUnitRepository extends MongoRepository<TransportationUnit, String> {


    Optional<TransportationUnit> findTransportationUnitByTrackingCode(String trackingCode);

}
