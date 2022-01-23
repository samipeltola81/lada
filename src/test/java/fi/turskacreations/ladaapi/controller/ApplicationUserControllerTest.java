package fi.turskacreations.ladaapi.controller;


import fi.turskacreations.ladaapi.db.MongoConfigurationCommon;
import fi.turskacreations.ladaapi.security.JWTGenerator;
import fi.turskacreations.ladaapi.domain.ApplicationUser;
import fi.turskacreations.ladaapi.security.WebSecurityConfiguration;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitService;
import fi.turskacreations.ladaapi.user.ApplicationUserController;
import fi.turskacreations.ladaapi.user.ApplicationUserService;
import fi.turskacreations.ladaapi.user.UserConfiguration;
import fi.turskacreations.ladaapi.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static fi.turskacreations.ladaapi.constants.Paths.APPLICATION_USER;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(value = ApplicationUserController.class, includeFilters = @ComponentScan.Filter(classes = EnableWebSecurity.class))
@WebMvcTest(value = ApplicationUserController.class)
//@Import({UserConfiguration.class, WebSecurityConfiguration.class})
public class ApplicationUserControllerTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("applicationUserDetailsServiceImpl")
    ApplicationUserService applicationUserService;

    @MockBean
    JWTGenerator jwtGenerator;

    @Test
    @WithMockUser
    public void signUp_with_correct_activationkey_succeeds_and_returns_OK() throws Exception{

        ApplicationUser applicationUser = ApplicationUser.builder().username("admin").activationKey("j9lAYY0SwGFuqs6aHddM").build();

        given(applicationUserService.activateUser(applicationUser.getActivationKey())).willReturn(applicationUser);
        given(jwtGenerator.generateToken(applicationUser.getUsername())).willReturn(applicationUser.getActivationKey());

        MvcResult result = mvc.perform(post(APPLICATION_USER + "/sign-up")
                .with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(applicationUser))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt", is(applicationUser.getActivationKey())))
                .andReturn()
                ;
        logger.info( "Results: {}", result.getResponse().getContentAsString());
        verify(applicationUserService,times(1)).activateUser(applicationUser.getActivationKey());
        verify(jwtGenerator,times(1)).generateToken(applicationUser.getUsername());

    }

    /*
    @Test
    public void signUp_with_incorrect_ActivationKey_fails_and_returns_Forbidden() throws Exception{

        ApplicationUser applicationUser = ApplicationUser.builder().username("admin").activationKey("j9lAYY0SwGFuqs6aHddM").build();

        given(applicationUserService.activateUser(applicationUser.getActivationKey())).willThrow(UsernameNotFoundException.class);
        given(jwtGenerator.generateToken(applicationUser.getUsername())).willReturn(applicationUser.getActivationKey());

        MvcResult result = mvc.perform(post(APPLICATION_USER + "/sign-up")
                //.with(user("admin").password("123456"))
                .content(TestUtil.convertObjectToJsonBytes(applicationUser))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn()
                ;
        logger.info( "Results: {}", result.getResponse().getContentAsString());
        verify(applicationUserService,times(1)).activateUser(applicationUser.getActivationKey());
        verify(jwtGenerator,times(0)).generateToken(applicationUser.getUsername());

    }
    */


    @Test
    @WithMockUser(username = "user1")
    public void changePassword_succeeds_and_returns_OK() throws Exception{

        String username="user1", password="uusi salakala";

        ApplicationUser applicationUser = ApplicationUser.builder()
                .username(username)
                .password(password)
                .build();

        MvcResult result = mvc.perform(put(APPLICATION_USER + "/{username}/password", username)
                .with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(applicationUser))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                ;
        logger.info( "Results: {}", result.getResponse().getContentAsString());

    }



    @Test
    @WithMockUser(username = "user1")
    public void changePassword_with_different_usernames_returns_unauthorized() throws Exception{

        String username="user2", password="uusi salakala";

        ApplicationUser applicationUser = ApplicationUser.builder()
                .username(username)
                .password(password)
                .build();

        MvcResult result = mvc.perform(put(APPLICATION_USER + "/{username}/password", "admin")
                .with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(applicationUser))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn()
                ;
        logger.info( "Results: {}", result.getResponse().getContentAsString());

    }


}
