package fi.turskacreations.ladaapi.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Bean
    @Qualifier("applicationUserDetailsServiceImpl")
    public ApplicationUserService ApplicationUserService(){
        return new ApplicationUserServiceImpl();
    }

}
