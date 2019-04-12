package de.heavenhr.hireme.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private static final String ENDPOINT_MESSAGE = "A resource with id %s, type %s could not be found.";

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(UUID id, String type) {
        super(String.format(ENDPOINT_MESSAGE, id, type));
    }
}
