package fi.turskacreations.ladaapi.controller;

import fi.turskacreations.ladaapi.domain.Event;
import fi.turskacreations.ladaapi.domain.EventType;
import fi.turskacreations.ladaapi.domain.Transport;
import fi.turskacreations.ladaapi.domain.TransportationUnit;
import fi.turskacreations.ladaapi.dto.TrackingCodeList;
import fi.turskacreations.ladaapi.transport.TransportController;
import fi.turskacreations.ladaapi.transport.TransportService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static fi.turskacreations.ladaapi.constants.Paths.API;
import static fi.turskacreations.ladaapi.constants.Paths.TRANSPORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TransportController.class)
public class TransportControllerTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransportService transportService;

    @Test
    @WithMockUser
    public void register_one_trackingCode() throws Exception{

        String trackingCode = "trackingCode1";
        TrackingCodeList trackingCodeList = TrackingCodeList.builder().trackingCodes(Arrays.asList(trackingCode)).build();
        Long transportId = Long.valueOf(1);

        Transport transport = Transport.builder().transportId(transportId).registeredTrackingCodes(Arrays.asList(trackingCode)).build();

        given(transportService.registerTransportationUnits(transportId, trackingCodeList)).willReturn(transport);

        MvcResult result = mvc.perform(post(API + TRANSPORT + "/{transportId}/transportation-units", transportId)
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trackingCodeList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transportId", is(transportId.intValue())))
                .andExpect(jsonPath("$.registeredTrackingCodes[0]", is(trackingCode.toString())))
                .andReturn()
                ;

        logger.info( "Results: {}", result.getResponse().getContentAsString());

    }

    @Test
    @WithMockUser
    public void register_event_to_transportation_unit() throws Exception{

        String trackingCode = "trackingCode1";
        Long transportId = Long.valueOf(1);
        HashMap<String,String> additionalInfo =  new HashMap<String, String>(){{put( "Event","Loaded to transport: " + transportId);}};
        Event event = Event.builder()
                .eventType(EventType.REGISTERED)
                .details("Freeform details to define the event")
                .additionalInfo(additionalInfo)
                .build();


        MvcResult result = mvc.perform(post(API + TRANSPORT + "/{transportId}/transportation-units/{trackingCode}/events", transportId, trackingCode)
                .with(csrf())
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isOk())
                .andReturn()
                ;


    }




}
