package com.gdg.jwtexample.controller;

import com.gdg.jwtexample.dto.idcard.IDCardResponse;
import com.gdg.jwtexample.dto.idcard.IDCardSaveRequest;
import com.gdg.jwtexample.dto.idcard.IDCardUpdateRequest;
import com.gdg.jwtexample.service.IDCardService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/idcards")
public class IDCardController {
    private final IDCardService service;

    public IDCardController(IDCardService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<IDCardResponse> save(Principal principal, @RequestBody @Valid IDCardSaveRequest req) {
        return ResponseEntity.ok(service.save(principal, req));
    }

    @GetMapping
    public ResponseEntity<List<IDCardResponse>> list(Principal principal) {
        return ResponseEntity.ok(service.list(principal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IDCardResponse> update(
            Principal principal, @PathVariable Long id, @RequestBody @Valid IDCardUpdateRequest req) {
        return ResponseEntity.ok(service.update(principal, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable Long id) {
        service.delete(principal, id);
        return ResponseEntity.noContent().build();
    }
}
