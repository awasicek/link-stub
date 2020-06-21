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

    public Optional<LinkStub> getLinkStub(String urlHash) {
        return linkStubRepository.findById(urlHash);
    }

    public LinkStub createLinkStub(String originalUrl) {
        return linkStubRepository.save(new LinkStub(originalUrl));
    }
}
