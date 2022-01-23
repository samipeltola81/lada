package fi.turskacreations.ladaapi.user;

import fi.turskacreations.ladaapi.exceptions.ActivationKeyNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ApplicationUserDetailsService extends UserDetailsService {

    UserDetails verifyActivationKey(String activationKey ) throws ActivationKeyNotFoundException;
}
