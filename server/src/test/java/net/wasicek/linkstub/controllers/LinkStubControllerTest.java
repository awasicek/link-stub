package net.wasicek.linkstub.controllers;

import net.wasicek.linkstub.models.LinkStub;
import net.wasicek.linkstub.services.LinkStubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkStubControllerTest {

    private static final String TEST_URL = "http://test.automation";

    @Mock
    private LinkStubService linkStubService;

    @InjectMocks
    private LinkStubController linkStubController;

    @Test
    public void createLinkStub_shouldCreateNewLinkStub_whenItDoesNotExist() throws URISyntaxException {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubService.getLinkStubByUrl(anyString())).thenReturn(Optional.empty());
        when(linkStubService.createLinkStub(anyString())).thenReturn(linkStub);

        ResponseEntity<LinkStub> response = linkStubController.createLinkStub(linkStub);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(linkStub, response.getBody());
    }

    @Test
    public void createLinkStub_shouldRespondWithExistingLinkStub_whenItAlreadyExists() throws URISyntaxException {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubService.getLinkStubByUrl(anyString())).thenReturn(Optional.of(linkStub));

        ResponseEntity<LinkStub> response = linkStubController.createLinkStub(linkStub);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(linkStub, response.getBody());
    }

    @Test
    public void redirectLinkStub_shouldRedirectToOriginalLink_whenUrlHashProvided() throws URISyntaxException {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubService.getLinkStubByHash(anyString())).thenReturn(Optional.of(linkStub));
        when(linkStubService.isLinkStubValid(eq(linkStub))).thenReturn(true);

        ResponseEntity<LinkStub> response = linkStubController.redirectLinkStub(linkStub.getUrlHash());
        assertEquals(HttpStatus.SEE_OTHER, response.getStatusCode());
        assertEquals(TEST_URL, response.getHeaders().getLocation().toString());
    }

    @Test
    public void redirectLinkStub_shouldProcessTheUseOfLinkStub_uponValidRedirect() throws URISyntaxException {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubService.getLinkStubByHash(anyString())).thenReturn(Optional.of(linkStub));
        when(linkStubService.isLinkStubValid(eq(linkStub))).thenReturn(true);

        linkStubController.redirectLinkStub(linkStub.getUrlHash());
        verify(linkStubService).processLinkStubUse(eq(linkStub));
    }

    @Test
    public void redirectLinkStub_shouldRespondGone_whenLinkStubIsInvalidated() throws URISyntaxException {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubService.getLinkStubByHash(anyString())).thenReturn(Optional.of(linkStub));
        when(linkStubService.isLinkStubValid(eq(linkStub))).thenReturn(false);

        ResponseEntity<LinkStub> response = linkStubController.redirectLinkStub(linkStub.getUrlHash());
        assertEquals(HttpStatus.GONE, response.getStatusCode());
    }

    @Test
    public void redirectLinkStub_shouldRespondNotFound_whenUrlHashHasNoEntry() {
        when(linkStubService.getLinkStubByHash(anyString())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            linkStubController.redirectLinkStub("nonExistentHash");
        });
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}