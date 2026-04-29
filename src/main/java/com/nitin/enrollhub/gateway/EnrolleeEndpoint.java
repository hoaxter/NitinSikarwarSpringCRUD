package com.nitin.enrollhub.gateway;

import com.nitin.enrollhub.domain.Enrollee;
import com.nitin.enrollhub.orchestration.EnrolleeCoordinator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST gateway for enrollee operations.
 * Base path: {@code /api/enrollees}
 *
 * @author Nitin
 */
@RestController
@RequestMapping("/api/enrollees")
public class EnrolleeEndpoint {

    private final EnrolleeCoordinator coordinator;

    public EnrolleeEndpoint(EnrolleeCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @PostMapping
    public ResponseEntity<Enrollee> handleCreate(@RequestBody Enrollee enrollee) {
        Enrollee created = coordinator.registerNew(enrollee);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getEnrolleeId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Enrollee>> handleList() {
        return ResponseEntity.ok(coordinator.fetchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollee> handleGet(@PathVariable("id") long enrolleeId) {
        return ResponseEntity.ok(coordinator.fetchSingle(enrolleeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollee> handleUpdate(@PathVariable("id") long enrolleeId,
                                                  @RequestBody Enrollee enrollee) {
        return ResponseEntity.ok(coordinator.revise(enrolleeId, enrollee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> handleRemove(@PathVariable("id") long enrolleeId) {
        coordinator.drop(enrolleeId);
        return ResponseEntity.noContent().build();
    }
}
