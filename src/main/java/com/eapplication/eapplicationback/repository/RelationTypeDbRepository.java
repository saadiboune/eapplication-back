package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.RelationTypeDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationTypeDbRepository extends JpaRepository<RelationTypeDb, Integer> {
}
