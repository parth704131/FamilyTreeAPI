package com.marketpulse.FamilyTree.repositories;

import com.marketpulse.FamilyTree.models.HouseHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseHoldRepository extends JpaRepository<HouseHold,Integer> {
}
