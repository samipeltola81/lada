package fi.turskacreations.ladaapi.controller;

import fi.turskacreations.ladaapi.domain.TransportationUnit;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitController;
import fi.turskacreations.ladaapi.transportation_unit.TransportationUnitService;
import fi.turskacreations.ladaapi.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static fi.turskacreations.ladaapi.constants.Paths.API;
import static fi.turskacreations.ladaapi.constants.Paths.TRANSPORTATION_UNIT;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TransportationUnitController.class)
public class TransportationUnitControllerTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransportationUnitService transportationUnitService;

    @Test
    @WithMockUser
    public void getTransportationUnit_succesfully_and_return_OK() throws Exception{

        String trackingCode = UUID.randomUUID().toString();
        TransportationUnit transportationUnit = TransportationUnit.builder()
                .trackingCode(trackingCode)
                .build();

        given(transportationUnitService.getTransportationUnit(trackingCode)).willReturn(transportationUnit);

        MvcResult result = mvc.perform(get(API + TRANSPORTATION_UNIT + "/{trackingCode}", trackingCode)
                .with(csrf())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingCode", is(trackingCode.toString())))
                .andReturn()
                ;
        logger.info( "Results: {}", result.getResponse().getContentAsString());

    }

    @Test
    @WithMockUser
    public void save_TransportationUnit_succesfully_and_return_OK() throws Exception{

        //String id = "b06b2e6d-2f12-4748-8147-62669ab1b161";
        String trackingCode = UUID.randomUUID().toString();
        TransportationUnit transportationUnit = TransportationUnit.builder()
                .trackingCode(trackingCode)
                .build();


        MvcResult result = mvc.perform(post(API + TRANSPORTATION_UNIT)
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transportationUnit))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                ;
        logger.info( "Results: {}", result.getResponse().getContentAsString());

    }



}
