package fi.turskacreations.ladaapi.user;

import fi.turskacreations.ladaapi.domain.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<ApplicationUser> findByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

    @Override
    public ApplicationUser activateUser(String activationKey) {

        ApplicationUser applicationUser = applicationUserRepository.findByActivationKey(activationKey)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for activation key: " + activationKey));

        //TODO: validate activation key still valid

        return applicationUser;
    }

    @Override
    public void changePassword(String username, String password) {

        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        applicationUser.setPassword(bCryptPasswordEncoder.encode(password));
        applicationUserRepository.save(applicationUser);
        logger.info("Password changed for username: {}", username);
    }

    @Override
    public void save(ApplicationUser applicationUser) {
        applicationUserRepository.save(applicationUser);
        logger.info("New user: {}, with id: {}", applicationUser.getUsername());
    }
}
