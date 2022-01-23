package fi.turskacreations.ladaapi.transport;

import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.exceptions.SequenceException;


public interface TransportRepositoryCustom {

    Long saveTransport(Transport transport) throws SequenceException;

}
