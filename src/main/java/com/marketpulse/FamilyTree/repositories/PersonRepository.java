package com.marketpulse.FamilyTree.repositories;

import com.marketpulse.FamilyTree.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
