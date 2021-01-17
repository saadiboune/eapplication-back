package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.InRelationDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InRelationDbRepository extends JpaRepository<InRelationDb, Integer> {
}
