package fi.turskacreations.ladaapi.sequence;

import fi.turskacreations.ladaapi.exceptions.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceServiceImpl implements SequenceService{

    @Autowired
    SequenceRepositoryCustom sequenceRepository;

    public long getNextSequenceId(String key) throws SequenceException {
        return sequenceRepository.getNextSequenceId(key);
    }
}
