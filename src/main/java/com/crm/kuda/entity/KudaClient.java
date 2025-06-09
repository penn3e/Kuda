package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "kuda_clients")
@Data
public class KudaClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ClientType type;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private ClientCategory categorie;

    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @Column(name = "nom_contact")
    private String nomContact;

    @Column(name = "fonction_contact")
    private String fonctionContact;

    private String email;
    private String telephone;
    private String adresse;
    private String commune;
    private String wilaya;
    private String pays;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "numero_rc")
    private String numeroRc;

    private String nif;

    @ManyToOne
    @JoinColumn(name = "statut_id")
    private ClientStatus statut;

    @Column(name = "date_creation", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation = new Date();

    @Column(name = "date_derniere_interaction")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDerniereInteraction;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
    }
}
