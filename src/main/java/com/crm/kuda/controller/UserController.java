package com.crm.kuda.controller;

import com.crm.kuda.entity.User;
import com.crm.kuda.entity.UserRole;
import com.crm.kuda.repository.UserRoleRepository;
import com.crm.kuda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.crm.kuda.service.UserDetailsImpl;
import com.crm.kuda.dto.MessageResponse;
import com.crm.kuda.dto.CreateUserRequest;
import com.crm.kuda.dto.UserResponseDto;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<User> users = userService.getAllUsers(pageable);
        Page<UserResponseDto> userDtos = users.map(UserResponseDto::new);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new UserResponseDto(user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest, Authentication authentication) {
        try {
            // Récupérer l'utilisateur authentifié depuis le token
            UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
            User currentUser = userService.getUserById(currentUserDetails.getId());
            
            // Créer le nouvel utilisateur
            User newUser = new User();
            newUser.setUsername(createUserRequest.getUsername());
            newUser.setEmail(createUserRequest.getEmail());
            newUser.setPassword(createUserRequest.getPassword());
            newUser.setFullName(createUserRequest.getFullName());
            
            // Convertir les codes de rôle en entités UserRole
            Set<UserRole> userRoles = new HashSet<>();
            if (createUserRequest.getRoles() != null && !createUserRequest.getRoles().isEmpty()) {
                for (String roleCode : createUserRequest.getRoles()) {
                    UserRole role = userRoleRepository.findByCode(roleCode)
                            .orElseThrow(() -> new RuntimeException("Rôle non trouvé: " + roleCode));
                    userRoles.add(role);
                }
            } else {
                // Rôle par défaut
                UserRole defaultRole = userRoleRepository.findByCode("ROLE_OPERATIONAL")
                        .orElseThrow(() -> new RuntimeException("Rôle par défaut non trouvé"));
                userRoles.add(defaultRole);
            }
            newUser.setRoles(userRoles);
            
            // Utiliser la nouvelle méthode avec creator
            User createdUser = userService.createUserWithCreator(newUser, currentUser);
            
            return ResponseEntity.ok(new UserResponseDto(createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/my-created-users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<UserResponseDto>> getMyCreatedUsers(Authentication authentication) {
        try {
            UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
            User currentUser = userService.getUserById(currentUserDetails.getId());
            
            List<User> createdUsers = userService.getUsersCreatedBy(currentUser);
            List<UserResponseDto> userDtos = createdUsers.stream()
                    .map(UserResponseDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/created-by/{creatorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getUsersCreatedBy(@PathVariable Long creatorId) {
        try {
            User creator = userService.getUserById(creatorId);
            List<User> createdUsers = userService.getUsersCreatedBy(creator);
            List<UserResponseDto> userDtos = createdUsers.stream()
                    .map(UserResponseDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(new UserResponseDto(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
        }
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<Map<String, Object>> getUserStats(Authentication authentication) {
        try {
            UserDetailsImpl currentUserDetails = (UserDetailsImpl) authentication.getPrincipal();
            User currentUser = userService.getUserById(currentUserDetails.getId());
            
            Map<String, Object> stats = new HashMap<>();
            
            // Statistiques pour l'utilisateur connecté
            List<User> myCreatedUsers = userService.getUsersCreatedBy(currentUser);
            stats.put("usersCreatedByMe", myCreatedUsers.size());
            
            // Statistiques globales (seulement pour les admins)
            if (currentUserDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                
                long totalUsers = userService.getTotalUsersCount();
                long adminCount = userService.getUsersByRoleCode("ROLE_ADMIN").size();
                long supervisorCount = userService.getUsersByRoleCode("ROLE_SUPERVISOR").size();
                long operationalCount = userService.getUsersByRoleCode("ROLE_OPERATIONAL").size();
                
                stats.put("totalUsers", totalUsers);
                stats.put("adminCount", adminCount);
                stats.put("supervisorCount", supervisorCount);
                stats.put("operationalCount", operationalCount);
            }
            
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
