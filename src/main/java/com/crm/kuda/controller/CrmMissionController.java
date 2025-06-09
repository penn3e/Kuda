package com.crm.kuda.controller;

import com.crm.kuda.entity.CrmMission;
import com.crm.kuda.entity.MissionStatusEnum;
import com.crm.kuda.service.CrmMissionService;
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
import org.springframework.security.core.Authentication;
import com.crm.kuda.service.UserDetailsImpl;
import com.crm.kuda.repository.UserRepository;
import com.crm.kuda.repository.InteractionTypeRepository;
import com.crm.kuda.dto.CreateMissionRequest;
import com.crm.kuda.entity.User;
import com.crm.kuda.entity.InteractionType;

@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "*")
public class CrmMissionController {

    @Autowired
    private CrmMissionService missionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InteractionTypeRepository interactionTypeRepository;

    @GetMapping
    public ResponseEntity<Page<CrmMission>> getAllMissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CrmMission> missions = missionService.getAllMissions(pageable);
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrmMission> getMissionById(@PathVariable Long id) {
        Optional<CrmMission> mission = missionService.getMissionById(id);
        return mission.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CrmMission> createMission(@Valid @RequestBody CreateMissionRequest missionRequest, Authentication authentication) {
        // Récupérer l'utilisateur authentifié
        UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findById(currentUserDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    
        // Convertir le DTO en entité
        CrmMission mission = new CrmMission();
    
        // Récupérer les références
        InteractionType typeInteraction = interactionTypeRepository.findById(missionRequest.getTypeInteractionId())
                .orElseThrow(() -> new RuntimeException("Type d'interaction non trouvé"));
        mission.setTypeInteraction(typeInteraction);
    
        User assignedUser = userRepository.findById(missionRequest.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Utilisateur assigné non trouvé"));
        mission.setAssignedTo(assignedUser);
    
        // Copier les propriétés
        mission.setTitre(missionRequest.getTitre());
        mission.setDescription(missionRequest.getDescription());
        mission.setDateDebut(missionRequest.getDateDebut());
        mission.setDateFin(missionRequest.getDateFin());
        mission.setStatut("EN_ATTENTE"); // Statut par défaut
    
        // Définir l'utilisateur créateur
        mission.setCreatedBy(currentUser);
    
        CrmMission savedMission = missionService.saveMission(mission);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrmMission> updateMission(@PathVariable Long id, @Valid @RequestBody CrmMission mission) {
        if (!missionService.getMissionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        mission.setId(id);
        CrmMission updatedMission = missionService.saveMission(mission);
        return ResponseEntity.ok(updatedMission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        if (!missionService.getMissionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CrmMission>> getMissionsByStatus(@PathVariable MissionStatusEnum status) {
        List<CrmMission> missions = missionService.getMissionsByStatus(status);
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<CrmMission>> getMissionsByAssignedUser(@PathVariable Long userId) {
        List<CrmMission> missions = missionService.getMissionsByAssignedUserId(userId);
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CrmMission>> getMissionsByClient(@PathVariable Long clientId) {
        List<CrmMission> missions = missionService.getMissionsByClientId(clientId);
        return ResponseEntity.ok(missions);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CrmMission> updateMissionStatus(@PathVariable Long id, @RequestBody MissionStatusEnum status) {
        Optional<CrmMission> updatedMission = missionService.updateMissionStatus(id, status);
        return updatedMission.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{missionId}/clients/{clientId}")
    public ResponseEntity<Void> addClientToMission(@PathVariable Long missionId, @PathVariable Long clientId) {
        boolean added = missionService.addClientToMission(missionId, clientId);
        return added ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{missionId}/clients/{clientId}")
    public ResponseEntity<Void> removeClientFromMission(@PathVariable Long missionId, @PathVariable Long clientId) {
        boolean removed = missionService.removeClientFromMission(missionId, clientId);
        return removed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
