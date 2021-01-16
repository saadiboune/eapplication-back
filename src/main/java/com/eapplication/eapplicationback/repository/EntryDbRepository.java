package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.EntryDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryDbRepository extends JpaRepository<EntryDb, Integer> {
}
