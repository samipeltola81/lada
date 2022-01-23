package fi.turskacreations.ladaapi.transportation_unit;

import fi.turskacreations.ladaapi.domain.TransportationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static fi.turskacreations.ladaapi.constants.Paths.API;
import static fi.turskacreations.ladaapi.constants.Paths.TRANSPORTATION_UNIT;

@RestController
@RequestMapping(API + TRANSPORTATION_UNIT)
public class TransportationUnitController {

    @Autowired
    TransportationUnitService transportationUnitService;

    /*
    @GetMapping( TRANSPORTATION_UNIT + "/{sequenceKey}")
    public TransportationUnit getSpecificTransportationUnit(@PathVariable UUID sequenceKey){

        //return transportationUnitService.getSpecificTransportationUnit(sequenceKey);

    }
    */

    @GetMapping("/{trackingCode}")
    public TransportationUnit getTransportationUnit(@PathVariable String trackingCode){

        return transportationUnitService.getTransportationUnit(trackingCode);

    }

    @GetMapping("")
    public List<TransportationUnit> getAllTransportationUnits(){

        return transportationUnitService.getAllTransportationUnits();

    }

    @PostMapping
    public void createTransportationUnit(@RequestBody TransportationUnit transportationUnit){

        transportationUnitService.save(transportationUnit);

    }

}
