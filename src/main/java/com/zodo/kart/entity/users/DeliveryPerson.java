package com.zodo.kart.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="DELIVERY_PERSON")
public class DeliveryPerson{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_info_id", referencedColumnName = "id")
    private Operator personalDetails;
    private String licenceNumber;
    private String vehicleNumber;
    private String vehicleType;
    private String vehicleModel;
    private String alternateMobileNumber;
    private String vehicleRegistrationNumber;
    private String vehicleImage;
    private boolean isVerified;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
