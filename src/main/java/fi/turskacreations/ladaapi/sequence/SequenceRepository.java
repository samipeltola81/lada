package fi.turskacreations.ladaapi.sequence;

import fi.turskacreations.ladaapi.domain.Sequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceRepository extends MongoRepository<Sequence, String>, SequenceRepositoryCustom{
}
