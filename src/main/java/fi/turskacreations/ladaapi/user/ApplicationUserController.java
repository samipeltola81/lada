package fi.turskacreations.ladaapi.user;


import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.dto.JWTTokenResponse;
import fi.turskacreations.ladaapi.security.JWTGenerator;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static fi.turskacreations.ladaapi.constants.Paths.APPLICATION_USER;
import static fi.turskacreations.ladaapi.security.SecurityConstants.*;


@RestController
@RequestMapping(value = APPLICATION_USER)
@Api(value = APPLICATION_USER)
public class ApplicationUserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private JWTGenerator jwtGenerator;


    @PostMapping("/sign-up")
    public ResponseEntity<JWTTokenResponse> signUp(@RequestBody ApplicationUser applicationUser ) {
        applicationUser = applicationUserService.activateUser(applicationUser.getActivationKey());
        String token = jwtGenerator.generateToken(applicationUser.getUsername());
        logger.info("Success! New SIGNED UP user: {}", applicationUser.getUsername());
        return ResponseEntity.ok().header(HEADER_STRING, TOKEN_PREFIX + token).body(JWTTokenResponse.builder().jwt(token).build());
    }


    @PutMapping("/{username}/password")
    public ResponseEntity<String> changePassword( @PathVariable(name = "username") String username, @RequestBody ApplicationUser applicationUser, Principal principal  ){

        if(!username.equals(principal.getName())){
            final String errorMsg = String.format("User {} is not authorized to update password for user: {}", principal.getName(), username);
            logger.error(errorMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
        }

        applicationUserService.changePassword(username, applicationUser.getPassword());
        return ResponseEntity.ok().build();
    }

}
