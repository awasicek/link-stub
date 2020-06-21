package net.wasicek.linkstub.services;

import net.wasicek.linkstub.models.LinkStub;
import net.wasicek.linkstub.repositories.LinkStubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkStubServiceTest {

    private static final String TEST_URL = "testUrl";
    private static final String TEST_HASH = "testHash";

    @Mock
    private LinkStubRepository linkStubRepository;

    @InjectMocks
    private LinkStubService linkStubService;

    @Test
    public void getLinkStubByHash_shouldFindLinkStub_whenItExists() {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubRepository.findById(anyString())).thenReturn(Optional.of(linkStub));

        LinkStub searchResultActual = linkStubService.getLinkStubByHash(linkStub.getUrlHash()).get();
        assertEquals(linkStub, searchResultActual);
    }

    @Test
    public void getLinkStubByHash_shouldNotFindLinkStub_whenItIsNotPresent() {
        when(linkStubRepository.findById(anyString())).thenReturn(Optional.empty());

        Optional<LinkStub> searchResultActual = linkStubService.getLinkStubByHash(TEST_HASH);
        assertEquals(Optional.empty(), searchResultActual);
    }

    @Test
    public void getLinkStubByUrl_shouldFindLinkStub_whenItExists() {
        LinkStub linkStub = new LinkStub(TEST_URL);
        when(linkStubRepository.findByOriginalUrl(anyString())).thenReturn(Optional.of(linkStub));

        LinkStub searchResultActual = linkStubService.getLinkStubByUrl(linkStub.getOriginalUrl()).get();
        assertEquals(linkStub, searchResultActual);
    }

    @Test
    public void getLinkStubByUrl_shouldNotFindLinkStub_whenItIsNotPresent() {
        when(linkStubRepository.findByOriginalUrl(anyString())).thenReturn(Optional.empty());

        Optional<LinkStub> searchResultActual = linkStubService.getLinkStubByUrl(TEST_HASH);
        assertEquals(Optional.empty(), searchResultActual);
    }

    @Test
    public void createLinkStub_shouldSaveLinkStubToDatabase_whenUrlNotAlreadyPresent() {
        LinkStub linkStub = new LinkStub(TEST_URL);

        linkStubService.createLinkStub(linkStub.getOriginalUrl());
        verify(linkStubRepository).save(eq(linkStub));
    }

    @Test
    public void createLinkStub_shouldReturnExistingLinkStubEntry_whenUrlAlreadyPresent() {
        LinkStub linkStub = new LinkStub(TEST_URL);

        when(linkStubRepository.findByOriginalUrl(eq(linkStub.getOriginalUrl()))).thenReturn(Optional.of(linkStub));
        LinkStub linkStubCreated = linkStubService.createLinkStub(linkStub.getOriginalUrl());

        verify(linkStubRepository, never()).save(any(LinkStub.class));
        assertEquals(linkStub, linkStubCreated);
    }
}