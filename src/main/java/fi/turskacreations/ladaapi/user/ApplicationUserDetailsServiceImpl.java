package fi.turskacreations.ladaapi.user;


import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.exceptions.ActivationKeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserDetailsServiceImpl implements ApplicationUserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationUserRepository applicationUserRepository;

    public ApplicationUserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails verifyActivationKey( String activationKey ) throws ActivationKeyNotFoundException{

        ApplicationUser applicationUser = applicationUserRepository.findByActivationKey(activationKey).orElseThrow(() -> new ActivationKeyNotFoundException(activationKey));

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());

    }
}
