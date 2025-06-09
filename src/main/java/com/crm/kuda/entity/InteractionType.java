package com.crm.kuda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "interaction_types")
@Data
public class InteractionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String label;

    public InteractionType() {}

    public InteractionType(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
