package com.zodo.kart.repository.inventory;

import com.zodo.kart.entity.inventory.IPPack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
* Author : Bhanu prasad
*/

@Repository
public interface IPPackRepository extends JpaRepository<IPPack, Long> {
}
