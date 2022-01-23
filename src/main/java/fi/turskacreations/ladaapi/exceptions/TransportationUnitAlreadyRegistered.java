package fi.turskacreations.ladaapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransportationUnitAlreadyRegistered extends RuntimeException{

    public TransportationUnitAlreadyRegistered(String message) {
        super(message);
    }

    public TransportationUnitAlreadyRegistered(String message, Throwable cause) {
        super(message, cause);
    }

    public TransportationUnitAlreadyRegistered(Throwable cause) {
        super(cause);
    }

    protected TransportationUnitAlreadyRegistered(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
