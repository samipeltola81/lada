package fi.turskacreations.ladaapi.db;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.domain.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static fi.turskacreations.ladaapi.constants.Sequences.TRANSPORTS_SEQUENCE_KEY;

@ChangeLog
public class MongoChangeLog {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ChangeSet(order = "001", id = "change-2018-03-10-001", author = "samip")
    public void initialize_Sequence_for_transports(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection(Sequence.class);
        Sequence sequenceInit = Sequence.builder().sequenceKey(TRANSPORTS_SEQUENCE_KEY).build();
        mongoTemplate.save(sequenceInit);
    }

    @ChangeSet(order = "002", id = "change-2018-03-10-002", author = "samip", runAlways = true)
    public void populate_user_data(MongoTemplate mongoTemplate) {
        String username = "admin";

        ApplicationUser testUser = mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), ApplicationUser.class);
        if(testUser == null){
            String password = "123456";
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            testUser  = ApplicationUser.builder()
                    .username(username)
                    .password(hashedPassword)
                    .activationKey("j9lAYY0SwGFuqs6aHddM")
                    .build()
                    ;
            mongoTemplate.save(testUser);
            logger.info("Creating test user: {}", username);
        }

    }



}
