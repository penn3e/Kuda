package com.crm.kuda.controller;

import com.crm.kuda.entity.CrmInteraction;
import com.crm.kuda.entity.InteractionTypeEnum;
import com.crm.kuda.service.CrmInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

// Ajouter les imports nécessaires
import org.springframework.security.core.Authentication;
import com.crm.kuda.service.UserDetailsImpl;
import com.crm.kuda.repository.UserRepository;
import com.crm.kuda.repository.KudaClientRepository;
import com.crm.kuda.repository.InteractionTypeRepository;
import com.crm.kuda.dto.CreateInteractionRequest;
import com.crm.kuda.entity.User;
import com.crm.kuda.entity.KudaClient;
import com.crm.kuda.entity.InteractionType;

@RestController
@RequestMapping("/api/interactions")
@CrossOrigin(origins = "*")
public class CrmInteractionController {

    @Autowired
    private CrmInteractionService interactionService;

    // Ajouter les repositories
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KudaClientRepository clientRepository;

    @Autowired
    private InteractionTypeRepository interactionTypeRepository;

    @GetMapping
    public ResponseEntity<Page<CrmInteraction>> getAllInteractions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateInteraction") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CrmInteraction> interactions = interactionService.getAllInteractions(pageable);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrmInteraction> getInteractionById(@PathVariable Long id) {
        Optional<CrmInteraction> interaction = interactionService.getInteractionById(id);
        return interaction.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // Modifier la méthode createInteraction
    @PostMapping
    public ResponseEntity<CrmInteraction> createInteraction(@Valid @RequestBody CreateInteractionRequest interactionRequest, Authentication authentication) {
        // Récupérer l'utilisateur authentifié
        UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findById(currentUserDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    
        // Convertir le DTO en entité
        CrmInteraction interaction = new CrmInteraction();
    
        // Récupérer les références
        KudaClient client = clientRepository.findById(interactionRequest.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        interaction.setClient(client);
    
        InteractionType type = interactionTypeRepository.findById(interactionRequest.getTypeId())
                .orElseThrow(() -> new RuntimeException("Type d'interaction non trouvé"));
        interaction.setType(type);
    
        // Copier les propriétés
        interaction.setCommentaire(interactionRequest.getCommentaire());
    
        // Définir l'utilisateur créateur
        interaction.setCreatedBy(currentUser);
    
        CrmInteraction savedInteraction = interactionService.saveInteraction(interaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInteraction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrmInteraction> updateInteraction(@PathVariable Long id, @Valid @RequestBody CrmInteraction interaction) {
        if (!interactionService.getInteractionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        interaction.setId(id);
        CrmInteraction updatedInteraction = interactionService.saveInteraction(interaction);
        return ResponseEntity.ok(updatedInteraction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraction(@PathVariable Long id) {
        if (!interactionService.getInteractionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        interactionService.deleteInteraction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<CrmInteraction>> getInteractionsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateInteraction").descending());
        Page<CrmInteraction> interactions = interactionService.getInteractionsByClientId(clientId, pageable);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CrmInteraction>> getInteractionsByType(@PathVariable InteractionTypeEnum type) {
        List<CrmInteraction> interactions = interactionService.getInteractionsByType(type);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CrmInteraction>> getInteractionsByUser(@PathVariable Long userId) {
        List<CrmInteraction> interactions = interactionService.getInteractionsByUserId(userId);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/stats/client/{clientId}")
    public ResponseEntity<Long> getInteractionCountByClient(@PathVariable Long clientId) {
        Long count = interactionService.getInteractionCountByClientId(clientId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/type/{type}")
    public ResponseEntity<Long> getInteractionCountByType(@PathVariable InteractionTypeEnum type) {
        Long count = interactionService.getInteractionCountByType(type);
        return ResponseEntity.ok(count);
    }
}
