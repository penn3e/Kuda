package com.crm.kuda.repository;

import com.crm.kuda.entity.CrmMission;
import com.crm.kuda.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CrmMissionRepository extends JpaRepository<CrmMission, Long> {
    
    List<CrmMission> findByStatut(String statut);
    
    List<CrmMission> findByAssignedTo(User assignedTo);
    
    List<CrmMission> findByCreatedBy(User createdBy);
    
    @Query("SELECT m FROM CrmMission m WHERE m.dateDebut >= :dateDebut AND m.dateFin <= :dateFin")
    List<CrmMission> findByDateRange(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    @Query("SELECT m FROM CrmMission m WHERE m.assignedTo = :user AND m.statut = :statut")
    Page<CrmMission> findByAssignedToAndStatut(@Param("user") User user, @Param("statut") String statut, Pageable pageable);
    
    @Query("SELECT m FROM CrmMission m JOIN m.clients mc WHERE mc.client.id = :clientId")
    List<CrmMission> findByClientId(@Param("clientId") Long clientId);
    
    long countByStatut(String statut);
    
    long countByAssignedToAndStatut(User assignedTo, String statut);
}
