package net.wasicek.linkstub.services;

import net.wasicek.linkstub.models.LinkStub;
import net.wasicek.linkstub.repositories.LinkStubRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinkStubService {

    public static final int MAX_NUM_USES = 10;

    private LinkStubRepository linkStubRepository;

    @Autowired
    public LinkStubService(LinkStubRepository linkStubRepository) {
        this.linkStubRepository = linkStubRepository;
    }

    public Optional<LinkStub> getLinkStubByHash(String urlHash) {
        return linkStubRepository.findById(urlHash);
    }

    public Optional<LinkStub> getLinkStubByUrl(String originalUrl) {
        return linkStubRepository.findByOriginalUrl(originalUrl);
    }

    public LinkStub createLinkStub(String originalUrl) {
        return linkStubRepository.save(new LinkStub(originalUrl));
    }

    public boolean isLinkStubValid(LinkStub linkStub) {
        return linkStub.getNumTimesUsed() < MAX_NUM_USES;
    }

    public void processLinkStubUse(LinkStub linkStub) {
        linkStub.setNumTimesUsed(linkStub.getNumTimesUsed() + 1);
        linkStubRepository.save(linkStub);
    }

    public void resetValidity(LinkStub linkStub) {
        linkStub.setNumTimesUsed(0);
        linkStubRepository.save(linkStub);
    }

    public boolean isValidUrl(String url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        return urlValidator.isValid(url);
    }
}
