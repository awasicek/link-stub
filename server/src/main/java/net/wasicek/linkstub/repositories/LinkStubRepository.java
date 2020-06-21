package net.wasicek.linkstub.repositories;

import net.wasicek.linkstub.models.LinkStub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkStubRepository extends JpaRepository<LinkStub, String> {
    Optional<LinkStub> findByOriginalUrl(String originalUrl);
}
