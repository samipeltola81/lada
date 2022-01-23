package fi.turskacreations.ladaapi.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EventType {

    REGISTERED("registered"),
    DEREGISTER("deregistered");

    private String type;


}
