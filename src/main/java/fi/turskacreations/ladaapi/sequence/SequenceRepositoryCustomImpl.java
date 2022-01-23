package fi.turskacreations.ladaapi.sequence;

import fi.turskacreations.ladaapi.domain.Sequence;
import fi.turskacreations.ladaapi.exceptions.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceRepositoryCustomImpl implements SequenceRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Long getNextSequenceId(String key) throws SequenceException {

        Query query = new Query(Criteria.where("sequenceKey").is(key));
        Update update = new Update().inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        Sequence seqId = mongoOperations.findAndModify(query, update, options, Sequence.class);

        if (seqId == null) {
            throw new SequenceException("Unable to get the next sequence sequenceKey for key: " + key);
        }

        return seqId.getSeq();
    }
}
