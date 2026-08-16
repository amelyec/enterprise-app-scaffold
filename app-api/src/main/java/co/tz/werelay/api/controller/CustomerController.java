package co.tz.werelay.api.controller;

import co.tz.werelay.api.dto.CreateCustomerRequest;
import co.tz.werelay.api.dto.CustomerResponse;
import co.tz.werelay.domain.model.Customer;
import co.tz.werelay.service.CustomerService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * URI-based API versioning ("/v1/...") — decided upfront so a future breaking
 * change ships as /v2/... alongside the old version, instead of breaking clients in place.
 */
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "default")
    public ResponseEntity<CustomerResponse> getById(@PathVariable String id) {
        Customer customer = customerService.getById(id);
        return ResponseEntity.ok(CustomerResponse.from(customer));
    }

    @PostMapping
    @RateLimiter(name = "default")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.create(request.fullName(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerResponse.from(customer));
    }
}
