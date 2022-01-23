package fi.turskacreations.ladaapi.sequence;

import fi.turskacreations.ladaapi.exceptions.SequenceException;

public interface SequenceRepositoryCustom {

    Long getNextSequenceId(String key) throws SequenceException;

}
