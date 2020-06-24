package net.wasicek.linkstub.models;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class LinkStub {

    @Id
    private String urlHash;

    @NonNull
    private String originalUrl;

    @CreationTimestamp
    private Instant createdOn;

    private int numTimesUsed;

    public LinkStub(String originalUrl) {
        HashCode hashCode = Hashing.murmur3_32().hashString(originalUrl, StandardCharsets.UTF_8);
        this.urlHash = hashCode.toString();
        this.originalUrl = originalUrl;
        this.numTimesUsed = 0;
    }
}