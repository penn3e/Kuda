package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client_status")
@Data
public class ClientStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String label;

    public ClientStatus() {}

    public ClientStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
