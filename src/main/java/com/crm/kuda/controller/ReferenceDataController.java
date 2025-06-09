package com.crm.kuda.controller;

import com.crm.kuda.entity.*;
import com.crm.kuda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reference")
@CrossOrigin(origins = "*")
public class ReferenceDataController {

    @Autowired
    private ClientTypeRepository clientTypeRepository;

    @Autowired
    private ClientStatusRepository clientStatusRepository;

    @Autowired
    private ClientCategoryRepository clientCategoryRepository;

    @Autowired
    private InteractionTypeRepository interactionTypeRepository;

    @GetMapping("/client-types")
    public ResponseEntity<List<ClientType>> getAllClientTypes() {
        List<ClientType> clientTypes = clientTypeRepository.findAll();
        return ResponseEntity.ok(clientTypes);
    }

    @GetMapping("/client-status")
    public ResponseEntity<List<ClientStatus>> getAllClientStatus() {
        List<ClientStatus> clientStatuses = clientStatusRepository.findAll();
        return ResponseEntity.ok(clientStatuses);
    }

    @GetMapping("/client-categories")
    public ResponseEntity<List<ClientCategory>> getAllClientCategories() {
        List<ClientCategory> clientCategories = clientCategoryRepository.findAll();
        return ResponseEntity.ok(clientCategories);
    }

    @GetMapping("/client-categories/by-famille")
    public ResponseEntity<Map<String, List<ClientCategory>>> getClientCategoriesByFamille() {
        List<ClientCategory> categories = clientCategoryRepository.findAll();
        Map<String, List<ClientCategory>> categoriesByFamille = categories.stream()
                .collect(Collectors.groupingBy(ClientCategory::getFamille));
        return ResponseEntity.ok(categoriesByFamille);
    }

    @GetMapping("/interaction-types")
    public ResponseEntity<List<InteractionType>> getAllInteractionTypes() {
        List<InteractionType> interactionTypes = interactionTypeRepository.findAll();
        return ResponseEntity.ok(interactionTypes);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllReferenceData() {
        Map<String, Object> referenceData = Map.of(
            "clientTypes", clientTypeRepository.findAll(),
            "clientStatuses", clientStatusRepository.findAll(),
            "clientCategories", clientCategoryRepository.findAll(),
            "interactionTypes", interactionTypeRepository.findAll()
        );
        return ResponseEntity.ok(referenceData);
    }
}
