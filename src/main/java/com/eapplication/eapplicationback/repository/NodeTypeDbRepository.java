package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.NodeTypeDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeTypeDbRepository extends JpaRepository<NodeTypeDb, Integer> {
}
