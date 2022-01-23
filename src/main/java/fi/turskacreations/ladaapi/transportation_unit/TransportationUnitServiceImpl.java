package fi.turskacreations.ladaapi.transportation_unit;

import fi.turskacreations.ladaapi.domain.TransportationUnit;
import fi.turskacreations.ladaapi.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransportationUnitServiceImpl implements TransportationUnitService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TransportationUnitRepository transportationUnitRepository;

    @Override
    public TransportationUnit getTransportationUnit(String trackingCode) {

        return transportationUnitRepository.findTransportationUnitByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Transportation unit not found with tracking code: %s", trackingCode.toString())));
    }

    @Override
    public List<TransportationUnit> getAllTransportationUnits() {
        return transportationUnitRepository.findAll();
    }

    @Override
    public TransportationUnit save(TransportationUnit transportationUnit) {

        if(transportationUnit.getTrackingCode() == null){
            transportationUnit.setTrackingCode(UUID.randomUUID().toString());
        }

        logger.debug("Saving transportation unit: {}", transportationUnit.getTrackingCode());
        transportationUnit = transportationUnitRepository.save(transportationUnit);

        return transportationUnit;
    }
}
