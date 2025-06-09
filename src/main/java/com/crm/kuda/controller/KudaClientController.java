package com.crm.kuda.controller;

import com.crm.kuda.dto.CreateClientRequest;
import com.crm.kuda.entity.*;
import com.crm.kuda.repository.ClientCategoryRepository;
import com.crm.kuda.repository.ClientStatusRepository;
import com.crm.kuda.repository.ClientTypeRepository;
import com.crm.kuda.repository.UserRepository;
import com.crm.kuda.service.KudaClientService;
import com.crm.kuda.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class KudaClientController {

    @Autowired
    private KudaClientService clientService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ClientTypeRepository clientTypeRepository;
    
    @Autowired
    private ClientCategoryRepository clientCategoryRepository;
    
    @Autowired
    private ClientStatusRepository clientStatusRepository;

    @GetMapping
    public ResponseEntity<Page<KudaClient>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<KudaClient> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KudaClient> getClientById(@PathVariable Long id) {
        Optional<KudaClient> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<KudaClient> createClient(@Valid @RequestBody CreateClientRequest clientRequest, Authentication authentication) {
        // Récupérer l'utilisateur authentifié
        UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findById(currentUserDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'email existe déjà
        if (clientRequest.getEmail() != null && clientService.existsByEmail(clientRequest.getEmail())) {
            return ResponseEntity.badRequest().body(null);
        }
        
        // Convertir le DTO en entité
        KudaClient client = new KudaClient();
        
        // Récupérer les références
        ClientType type = clientTypeRepository.findById(clientRequest.getTypeId())
                .orElseThrow(() -> new RuntimeException("Type de client non trouvé"));
        client.setType(type);
        
        if (clientRequest.getCategorieId() != null) {
            ClientCategory categorie = clientCategoryRepository.findById(clientRequest.getCategorieId())
                    .orElse(null);
            client.setCategorie(categorie);
        }
        
        if (clientRequest.getStatutId() != null) {
            ClientStatus statut = clientStatusRepository.findById(clientRequest.getStatutId())
                    .orElse(null);
            client.setStatut(statut);
        } else {
            // Statut par défaut "NOUVEAU"
            clientStatusRepository.findByCode("NOUVEAU").ifPresent(client::setStatut);
        }
        
        // Copier les propriétés
        client.setRaisonSociale(clientRequest.getRaisonSociale());
        client.setNomContact(clientRequest.getNomContact());
        client.setFonctionContact(clientRequest.getFonctionContact());
        client.setEmail(clientRequest.getEmail());
        client.setTelephone(clientRequest.getTelephone());
        client.setAdresse(clientRequest.getAdresse());
        client.setCommune(clientRequest.getCommune());
        client.setWilaya(clientRequest.getWilaya());
        client.setPays(clientRequest.getPays());
        client.setCodePostal(clientRequest.getCodePostal());
        client.setNumeroRc(clientRequest.getNumeroRc());
        client.setNif(clientRequest.getNif());
        client.setNotes(clientRequest.getNotes());
        
        // Définir l'utilisateur créateur
        client.setCreatedBy(currentUser);
        
        KudaClient savedClient = clientService.saveClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KudaClient> updateClient(@PathVariable Long id, @Valid @RequestBody KudaClient client) {
        if (!clientService.getClientById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        client.setId(id);
        KudaClient updatedClient = clientService.saveClient(client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientService.getClientById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<KudaClient>> searchClients(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<KudaClient> clients = clientService.searchClients(term, pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<KudaClient>> getClientsByType(@PathVariable Long typeId) {
        ClientType type = clientTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Type de client non trouvé"));
        List<KudaClient> clients = clientService.getClientsByType(type);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<KudaClient>> getClientsByStatus(@PathVariable Long statusId) {
        ClientStatus status = clientStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Statut de client non trouvé"));
        List<KudaClient> clients = clientService.getClientsByStatus(status);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/wilaya/{wilaya}")
    public ResponseEntity<List<KudaClient>> getClientsByWilaya(@PathVariable String wilaya) {
        List<KudaClient> clients = clientService.getClientsByWilaya(wilaya);
        return ResponseEntity.ok(clients);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<KudaClient> updateClientStatus(@PathVariable Long id, @RequestBody Long statusId) {
        ClientStatus status = clientStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Statut de client non trouvé"));
        Optional<KudaClient> updatedClient = clientService.updateClientStatus(id, status);
        return updatedClient.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/type")
    public ResponseEntity<Long> getClientCountByType(@RequestParam Long typeId) {
        ClientType type = clientTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Type de client non trouvé"));
        Long count = clientService.getClientCountByType(type);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/status")
    public ResponseEntity<Long> getClientCountByStatus(@RequestParam Long statusId) {
        ClientStatus status = clientStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Statut de client non trouvé"));
        Long count = clientService.getClientCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = clientService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
