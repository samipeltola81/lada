package fi.turskacreations.ladaapi.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnAuthorizedActionException extends RuntimeException{


    public UnAuthorizedActionException(String message) {
        super(message);
    }
}
