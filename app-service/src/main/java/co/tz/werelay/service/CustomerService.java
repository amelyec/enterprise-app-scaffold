package co.tz.werelay.service;

import co.tz.werelay.common.exception.ResourceNotFoundException;
import co.tz.werelay.domain.model.Customer;
import co.tz.werelay.persistence.entity.CustomerEntity;
import co.tz.werelay.persistence.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Customer getById(String id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return toDomain(entity);
    }

    @Transactional
    public Customer create(String fullName, String email) {
        CustomerEntity entity = new CustomerEntity(UUID.randomUUID().toString(), fullName, email, Instant.now());
        return toDomain(customerRepository.save(entity));
    }

    private Customer toDomain(CustomerEntity entity) {
        return new Customer(entity.getId(), entity.getFullName(), entity.getEmail(), entity.getCreatedAt());
    }
}
