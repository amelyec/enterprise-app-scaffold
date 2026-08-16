package co.tz.werelay.api.dto;

import co.tz.werelay.domain.model.Customer;

import java.time.Instant;

public record CustomerResponse(String id, String fullName, String email, Instant createdAt) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getFullName(), customer.getEmail(), customer.getCreatedAt());
    }
}
