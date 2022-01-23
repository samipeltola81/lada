package fi.turskacreations.ladaapi.user;

import fi.turskacreations.ladaapi.domain.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserService {

    Optional<ApplicationUser> findByUsername(String username);

    ApplicationUser activateUser( String activationKey );

    void changePassword(String username, String password);

    void save( ApplicationUser applicationUser );

}
