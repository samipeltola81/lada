package fi.turskacreations.ladaapi.security;


import fi.turskacreations.ladaapi.user.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static fi.turskacreations.ladaapi.security.SecurityConstants.*;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("applicationUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    */


    /*
    public WebSecurityConfiguration(@Qualifier("applicationUserDetailsServiceImpl") UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    */


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Prevent http sessions
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                    //.antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                    .antMatchers(ACTUATOR_URL).permitAll()
                    .and()
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers(AUTH_WHITELIST);

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
