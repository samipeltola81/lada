package fi.turskacreations.ladaapi.user;

import fi.turskacreations.ladaapi.domain.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
    Optional<ApplicationUser> findByUsername(String username);
    Optional<ApplicationUser> findByActivationKey(String activationKey);

}
