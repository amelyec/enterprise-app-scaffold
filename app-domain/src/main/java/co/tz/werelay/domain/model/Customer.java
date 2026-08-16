package co.tz.werelay.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Pure domain model — no JPA/Jackson/Spring annotations here on purpose.
 * Mapping to persistence/API shapes happens in app-persistence and app-api respectively.
 * Keeping this module framework-free is what makes it portable if this bounded
 * context ever needs to become a standalone microservice.
 */
public final class Customer {

    private final String id;
    private final String fullName;
    private final String email;
    private final Instant createdAt;

    public Customer(String id, String fullName, String email, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.fullName = Objects.requireNonNull(fullName, "fullName must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
