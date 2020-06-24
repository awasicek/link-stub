package net.wasicek.linkstub.controllers;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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
        Optional<LinkStub> searchResult = linkStubService.getLinkStubByUrl(linkStub.getOriginalUrl());
        ResponseEntity<LinkStub> response;
        if (searchResult.isEmpty()) {
            // 201 if created new
            LinkStub saveResult = linkStubService.createLinkStub(linkStub.getOriginalUrl());
            response = ResponseEntity
                    .created(new URI("/api/linkstub/" + saveResult.getUrlHash()))
                    .body(saveResult);
        } else {
            LinkStub foundStub = searchResult.get();
            linkStubService.resetValidity(foundStub);
            // 200 if already existed
            response = ResponseEntity.ok().body(foundStub);
        }
        return response;
    }

    @GetMapping("/{urlHash}")
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
                // need to disable caching so requests to recreated links work
                response = ResponseEntity.status(HttpStatus.GONE).cacheControl(CacheControl.noCache()).body(foundStub);
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
