package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.EntryDb;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryDbRepository extends JpaRepository<EntryDb, Integer> {

    @Query("SELECT ed FROM EntryDb ed WHERE ed.id IN " +
            "(SELECT ord.outNodeId from OutRelationDb ord where " +
            "ord.relationTypeId = ?1 and ord.nodeId = ?2 and ord.weight >= 0)")
    Page<EntryDb> findEntriesWithSpecificRelationType(int relationTypeId, int nodeIdSearched, Pageable pageable);

    void deleteByIdIn(List<Integer> ids);

//    @Query("SELECT ed FROM EntryDb ed WHERE ed.id IN (SELECT ord.outNodeId from OutRelationDb ord where ord.relationTypeId = ?1 and ord.nodeId = ?2 and ord.weight >= 0)")
//    Page<HomeModel> findHomePageWithNodeName(String name);

    @Query("SELECT edb.formatedName From EntryDb edb WHERE edb.id = ?1")
    String getFormatedNameById(int id);

    @Query("SELECT DISTINCT edb.id FROM EntryDb edb WHERE edb.name = ?1")
    List<Integer> findIdByName(String searchedWord);
}
