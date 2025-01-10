package com.zodo.kart.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="SELLER")
public class Seller{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_info_id", referencedColumnName = "id")
    private Operator personalInfo;
    private boolean isVerified;

    private String sellerInventoryId;

    private String region;
    private String regionCode;

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<DeliveryPerson> deliveryPersons;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admin_id",referencedColumnName = "id")
    private Admin admin;

}
