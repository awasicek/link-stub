package net.wasicek.linkstub.repositories;

import net.wasicek.linkstub.models.LinkStub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkStubRepository extends JpaRepository<LinkStub, String> {
}
