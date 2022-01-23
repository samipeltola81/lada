package fi.turskacreations.ladaapi.transport;

import fi.turskacreations.ladaapi.domain.Transport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportRepository extends MongoRepository<Transport, String>, TransportRepositoryCustom {

    Optional<Transport> findTransportByTransportId(Long transportId);

}
