package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "crm_interactions")
@Data
public class CrmInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private KudaClient client;

    @Column(name = "date_interaction")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInteraction = new Date();

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private InteractionType type;

    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        dateInteraction = new Date();
    }
}
