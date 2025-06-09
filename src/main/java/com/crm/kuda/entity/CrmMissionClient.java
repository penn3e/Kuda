package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "crm_mission_clients")
@Data
public class CrmMissionClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private CrmMission mission;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private KudaClient client;
}
