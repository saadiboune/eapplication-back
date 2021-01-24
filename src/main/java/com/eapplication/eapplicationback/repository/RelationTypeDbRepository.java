package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.RelationTypeDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationTypeDbRepository extends JpaRepository<RelationTypeDb, Integer> {

    void deleteByIdIn(List<Integer> ids);

    @Query("SELECT rtd.id from RelationTypeDb rtd where rtd.name = ?1")
    int findIdByName(String name);

    @Query("SELECT rtd.name from RelationTypeDb rtd where rtd.id = ?1")
    String findNameById(int id);

    @Query("SELECT rtdb FROM RelationTypeDb rtdb WHERE rtdb.id IN " +
            "(SELECT DISTINCT ordb.relationTypeId FROM OutRelationDb ordb WHERE ordb.nodeId = ?1)")
    List<RelationTypeDb> getBySearchedWord(int searchedWord);
}
