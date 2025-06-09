package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client_categories")
@Data
public class ClientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String famille;

    @Column(nullable = false)
    private String label;

    public ClientCategory() {}

    public ClientCategory(String code, String famille, String label) {
        this.code = code;
        this.famille = famille;
        this.label = label;
    }
}
