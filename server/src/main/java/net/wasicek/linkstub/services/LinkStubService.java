package net.wasicek.linkstub.services;

import net.wasicek.linkstub.models.LinkStub;
import net.wasicek.linkstub.repositories.LinkStubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinkStubService {

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
        LinkStub linkStubInDatabase;
        Optional<LinkStub> searchResult = linkStubRepository.findByOriginalUrl(originalUrl);
        if (searchResult.isEmpty()) {
            linkStubInDatabase = linkStubRepository.save(new LinkStub(originalUrl));
        } else {
            linkStubInDatabase = searchResult.get();
        }
        return linkStubInDatabase;
    }
}
