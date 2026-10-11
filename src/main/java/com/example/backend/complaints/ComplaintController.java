
package com.example.backend.complaints;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:5173")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    public record ComplaintRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 50) String category,
        @NotBlank @Size(max = 150) String subject,
        @NotBlank @Size(max = 3000) String description
    ) {}

    @GetMapping
    public List<Complaint> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Complaint getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Complaint create(
            @Valid @RequestBody ComplaintRequest request) {

        Complaint complaint = new Complaint();
        complaint.setName(request.name());
        complaint.setEmail(request.email());
        complaint.setCategory(request.category());
        complaint.setSubject(request.subject());
        complaint.setDescription(request.description());

        return service.create(complaint);
    }

    @PatchMapping("/{id}/status")
    public Complaint updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return service.updateStatus(
            id, body.get("status"));
    }
}

