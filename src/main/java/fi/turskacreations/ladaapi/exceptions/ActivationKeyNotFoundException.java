package fi.turskacreations.ladaapi.exceptions;


import org.springframework.security.core.AuthenticationException;

public class ActivationKeyNotFoundException extends AuthenticationException {


    public ActivationKeyNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public ActivationKeyNotFoundException(String msg) {
        super(msg);
    }
}
