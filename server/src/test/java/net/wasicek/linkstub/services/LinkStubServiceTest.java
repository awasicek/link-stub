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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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
    public void createLinkStub_shouldSaveLinkStubToDatabase_whenRequested() {
        LinkStub linkStub = new LinkStub(TEST_URL);

        linkStubService.createLinkStub(linkStub.getOriginalUrl());
        verify(linkStubRepository).save(eq(linkStub));
    }

    @Test
    public void isLinkStubValid_shouldReturnValid_whenNumberTimesUsedLessThanMax() {
        LinkStub linkStub = new LinkStub(TEST_URL);
        linkStub.setNumTimesUsed(LinkStubService.MAX_NUM_USES - 1);

        boolean isValid = linkStubService.isLinkStubValid(linkStub);
        assertTrue(isValid);
    }

    @Test
    public void isLinkStubValid_shouldReturnInvalid_whenNumberTimesUsedGreaterThanMax() {
        LinkStub linkStub = new LinkStub(TEST_URL);
        linkStub.setNumTimesUsed(LinkStubService.MAX_NUM_USES + 1);

        boolean isValid = linkStubService.isLinkStubValid(linkStub);
        assertFalse(isValid);
    }

    @Test
    public void isLinkStubValid_shouldReturnInvalid_whenNumberTimesUsedEqualToMax() {
        LinkStub linkStub = new LinkStub(TEST_URL);
        linkStub.setNumTimesUsed(LinkStubService.MAX_NUM_USES);

        boolean isValid = linkStubService.isLinkStubValid(linkStub);
        assertFalse(isValid);
    }

    @Test
    public void processLinkStubUse_shouldIncrementAndPersistTheNumberOfTimesUsed_whenRequested() {
        LinkStub linkStub = new LinkStub(TEST_URL);

        linkStubService.processLinkStubUse(linkStub);
        assertEquals(linkStub.getNumTimesUsed(), 1);
        linkStubService.processLinkStubUse(linkStub);
        assertEquals(linkStub.getNumTimesUsed(), 2);
        verify(linkStubRepository, times(2)).save(eq(linkStub));
    }

    @Test
    public void resetValidity_shouldSaveLinkStubWithZeroNumTimesUsed_whenRequested() {
        LinkStub actualSaved = new LinkStub(TEST_URL);
        actualSaved.setNumTimesUsed(5);
        LinkStub expectedSaved = new LinkStub(TEST_URL);
        expectedSaved.setNumTimesUsed(0);

        linkStubService.resetValidity(actualSaved);
        verify(linkStubRepository).save(eq(expectedSaved));
    }

    @Test
    public void isValidUrl_shouldReturnTrue_forValidHttpUrl() {
        boolean isValid = linkStubService.isValidUrl("http://test.com");

        assertTrue(isValid);
    }

    @Test
    public void isValidUrl_shouldReturnTrue_forValidHttpsUrl() {
        boolean isValid = linkStubService.isValidUrl("https://test.com");

        assertTrue(isValid);
    }

    @Test
    public void isValidUrl_shouldReturnFalse_forInvalidWWWUrl() {
        boolean isValid = linkStubService.isValidUrl("www.test.com");

        assertFalse(isValid);
    }

    @Test
    public void isValidUrl_shouldReturnFalse_forInvalidNoPrefixUrl() {
        boolean isValid = linkStubService.isValidUrl("test.com");

        assertFalse(isValid);
    }

    @Test
    public void isValidUrl_shouldReturnFalse_forOtherwiseValidFtpUrl() {
        boolean isValid = linkStubService.isValidUrl("ftp://test.com");

        assertFalse(isValid);
    }
}