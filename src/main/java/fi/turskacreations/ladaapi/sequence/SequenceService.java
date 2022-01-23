package fi.turskacreations.ladaapi.sequence;

import fi.turskacreations.ladaapi.exceptions.SequenceException;
import org.springframework.stereotype.Service;


public interface SequenceService {

    long getNextSequenceId(String key) throws SequenceException;

}
