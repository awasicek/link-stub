package net.wasicek.linkstub.controllers;

import lombok.extern.slf4j.Slf4j;
import net.wasicek.linkstub.exceptions.InvalidLinkStubException;
import net.wasicek.linkstub.models.LinkStub;
import net.wasicek.linkstub.services.LinkStubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@Slf4j
public class LinkStubController {

    private LinkStubService linkStubService;

    @Autowired
    public LinkStubController(LinkStubService linkStubService) {
        this.linkStubService = linkStubService;
    }

    @PostMapping("/linkstub")
    ResponseEntity<LinkStub> createLinkStub(@Valid @RequestBody LinkStub linkStub) throws URISyntaxException {
        log.info("Request to create link stub: {}", linkStub);
        String providedUrl = linkStub.getOriginalUrl();
        if (!linkStubService.isValidUrl(providedUrl)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid URL: %s", providedUrl));
        }
        Optional<LinkStub> searchResult = linkStubService.getLinkStubByUrl(providedUrl);
        ResponseEntity<LinkStub> response;
        if (searchResult.isEmpty()) {
            // 201 if created new
            LinkStub saveResult = linkStubService.createLinkStub(providedUrl);
            response = ResponseEntity
                    .created(new URI("/linkstub/" + saveResult.getUrlHash()))
                    .body(saveResult);
        } else {
            LinkStub foundStub = searchResult.get();
            linkStubService.resetValidity(foundStub);
            // 200 if already existed
            response = ResponseEntity.ok().body(foundStub);
        }
        return response;
    }

    @GetMapping("/{urlHash:[a-zA-Z0-9]{8}}")
    public ResponseEntity<LinkStub> redirectLinkStub(@PathVariable String urlHash) throws URISyntaxException {
        Optional<LinkStub> searchResult = linkStubService.getLinkStubByHash(urlHash);
        ResponseEntity<LinkStub> response;
        log.info("Redirect request for URL hash: {}", urlHash);
        if (searchResult.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Link Stub not found for %s.", urlHash)
            );
        } else {
            LinkStub foundStub = searchResult.get();
            if (!linkStubService.isLinkStubValid(foundStub)) {
                throw new InvalidLinkStubException(String.format("The Link Stub http://linkstub.ninja/%s has expired.", urlHash));
            } else {
                URI originalLocation = new URI(foundStub.getOriginalUrl());
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(originalLocation);
                linkStubService.processLinkStubUse(foundStub);
                response = new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            }
        }
        return response;
     }
}
