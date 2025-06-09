package com.crm.kuda.repository;

import com.crm.kuda.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:search% OR u.username LIKE %:search% OR u.email LIKE %:search%")
    Page<User> findBySearchTerm(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.code = :roleCode")
    List<User> findByRoleCode(@Param("roleCode") String roleCode);
    
    List<User> findByCreatedBy(User createdBy);
}
