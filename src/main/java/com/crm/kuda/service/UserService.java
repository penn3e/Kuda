package com.crm.kuda.service;

import com.crm.kuda.entity.Role;
import com.crm.kuda.entity.User;
import com.crm.kuda.repository.RoleRepository;
import com.crm.kuda.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
    }

    public long getTotalUsersCount() {
        return userRepository.count();
    }

    public List<User> getAllUsersList() {
        return userRepository.findAll();
    }

    public List<User> getUsersCreatedBy(User creator) {
        return userRepository.findByCreatedBy(creator);
    }

    public List<User> getUsersByRole(ERole roleName) {
        return userRepository.findByRoleName(roleName.name());
    }

    @Transactional
    public User createUser(User user, Set<String> strRoles) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles(strRoles));

        return userRepository.save(user);
    }

    @Transactional
    public User createUserWithCreator(User user, Set<String> strRoles, User creator) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles(strRoles));
        user.setCreatedBy(creator); // Définir le créateur

        return userRepository.save(user);
    }

    private Set<Role> getRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(roleRepository.findByName(ERole.ROLE_OPERATIONAL)
                    .orElseThrow(() -> new RuntimeException("Error: Default role not found.")));
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
                        roles.add(adminRole);
                        break;
                    case "sup":
                        Role modRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                                .orElseThrow(() -> new RuntimeException("Error: Moderator role not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_OPERATIONAL)
                                .orElseThrow(() -> new RuntimeException("Error: User role not found."));
                        roles.add(userRole);
                }
            });
        }

        return roles;
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        if (!user.getUsername().equals(userDetails.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Error: Username is already taken!");
            }
            user.setUsername(userDetails.getUsername());
        }

        if (!user.getEmail().equals(userDetails.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Error: Email is already in use!");
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        user.setFullName(userDetails.getFullName());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
