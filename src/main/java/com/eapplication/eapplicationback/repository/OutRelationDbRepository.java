package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutRelationDbRepository extends JpaRepository<OutRelationDb, Integer> {
}
