package net.wasicek.linkstub.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class InvalidLinkStubException extends RuntimeException {

    @Getter
    private HttpStatus status = HttpStatus.GONE;

    public InvalidLinkStubException(String message){
        super(message);
    }

    public InvalidLinkStubException(String message, Throwable cause){
        super(message, cause);
    }
}
