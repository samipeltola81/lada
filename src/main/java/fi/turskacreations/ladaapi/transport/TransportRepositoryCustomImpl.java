package fi.turskacreations.ladaapi.transport;

import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.exceptions.SequenceException;
import fi.turskacreations.ladaapi.sequence.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static fi.turskacreations.ladaapi.constants.Sequences.TRANSPORTS_SEQUENCE_KEY;

@Repository
public class TransportRepositoryCustomImpl implements TransportRepositoryCustom{

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SequenceRepository sequenceRepository;


    @Override
    public Long saveTransport(Transport transport) throws SequenceException {

        if(transport.getTransportId() == null || transport.getTransportId().equals("")){
            Long seqId = sequenceRepository.getNextSequenceId(TRANSPORTS_SEQUENCE_KEY);
            transport.setTransportId(seqId);
        }

        mongoTemplate.save(transport);

        return transport.getTransportId();
    }
}
