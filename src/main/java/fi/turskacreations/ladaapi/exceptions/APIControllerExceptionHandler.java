package fi.turskacreations.ladaapi.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = RestController.class)
public class APIControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResourceNotFoundException.class, UsernameNotFoundException.class, NullPointerException.class})
    protected ResponseEntity<Object> handleMissingResource(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorMessage(bodyOfResponse).build();
        return handleExceptionInternal(ex, exceptionResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { TransportationUnitAlreadyRegistered.class})
    protected ResponseEntity<Object> handleBadRequests(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorMessage(bodyOfResponse).build();
        return handleExceptionInternal(ex, exceptionResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UnAuthorizedActionException.class})
    protected ResponseEntity<Object> handleAuthorizationExceptions(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorMessage(bodyOfResponse).build();
        return handleExceptionInternal(ex, exceptionResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

}
