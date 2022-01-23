package fi.turskacreations.ladaapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event{

    private EventType eventType;
    private String details;
    private HashMap<String,String> additionalInfo;

    private String user;
    private Instant createdDate;


}
