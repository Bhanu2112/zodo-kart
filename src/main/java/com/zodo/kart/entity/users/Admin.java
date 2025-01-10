package com.zodo.kart.entity.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ADMIN")
public class Admin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_info_id", referencedColumnName = "id")
    private Operator personalInfo;

    private String adminInventoryId;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<SubAdmin> subAdmins;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Seller> sellers;
}
