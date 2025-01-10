package com.zodo.kart.entity.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ZONE")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;
    private String zoneName;
    private String zoneDescription;
    private String zoneSellerId;

    @OneToMany(mappedBy = "zone",cascade = CascadeType.ALL)
    private List<Coordinate> zoneCoordinates;

}
