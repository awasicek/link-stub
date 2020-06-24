package net.wasicek.linkstub.exceptions;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { InvalidLinkStubException.class})
    protected ResponseEntity<Object> handleInvalidLinkStubRedirect(InvalidLinkStubException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        // need to disable caching so requests to recreated links work
        headers.setCacheControl(CacheControl.noCache());
        return handleExceptionInternal(ex, ex.getMessage(), headers, ex.getStatus(), request);
    }
}