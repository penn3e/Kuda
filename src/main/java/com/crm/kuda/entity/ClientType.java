package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client_types")
@Data
public class ClientType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String label;

    public ClientType() {}

    public ClientType(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
