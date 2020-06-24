package net.wasicek.linkstub.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    @Mock
    private HttpServletRequest mockRequest;

    private CustomErrorController customErrorController = new CustomErrorController();

    @Test
    public void handleError_shouldReturnDefaultErrorPage_whenNoErrorPageMapped() {
        // 418 i am not a teapot error
        when(mockRequest.getAttribute(any())).thenReturn("418");

        String response = customErrorController.handleError(mockRequest);
        assertEquals("error", response);
    }

    @Test
    public void handleError_shouldReturnInternalServerErrorPage_whenOccurs() {
        when(mockRequest.getAttribute(any())).thenReturn("500");

        String response = customErrorController.handleError(mockRequest);
        assertEquals("error-500", response);
    }

    @Test
    public void handleError_shouldReturnBadRequestErrorPage_whenOccurs() {
        when(mockRequest.getAttribute(any())).thenReturn("400");

        String response = customErrorController.handleError(mockRequest);
        assertEquals("error-400", response);
    }

    @Test
    public void handleError_shouldReturnGoneErrorPage_whenOccurs() {
        when(mockRequest.getAttribute(any())).thenReturn("410");

        String response = customErrorController.handleError(mockRequest);
        assertEquals("error-410", response);
    }

    @Test
    public void handleError_shouldNotFoundErrorPage_whenOccurs() {
        when(mockRequest.getAttribute(any())).thenReturn("404");

        String response = customErrorController.handleError(mockRequest);
        assertEquals("error-404", response);
    }
}