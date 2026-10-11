
package com.example.backend.complaints;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ComplaintService {

    private final ComplaintRepository repository;

    public ComplaintService(ComplaintRepository repository) {
        this.repository = repository;
    }

    public Complaint create(Complaint complaint) {
        complaint.setId(null);
        complaint.setStatus("PENDING");
        complaint.setCreatedAt(null);

        return repository.save(complaint);
    }

    public List<Complaint> findAll() {
        return repository.findAll();
    }

    public Complaint findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Complaint not found"
                    )
                );
    }

    public Complaint updateStatus(Long id, String status) {
        List<String> allowedStatuses = List.of(
            "PENDING",
            "IN_PROGRESS",
            "RESOLVED",
            "REJECTED"
        );

        if (status == null || !allowedStatuses.contains(status)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid complaint status"
            );
        }

        Complaint complaint = findById(id);
        complaint.setStatus(status);

        return repository.save(complaint);
    }
}
