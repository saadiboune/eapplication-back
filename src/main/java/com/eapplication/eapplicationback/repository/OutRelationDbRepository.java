package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OutRelationDbRepository extends JpaRepository<OutRelationDb, Integer> {
    void deleteByIdIn(List<Integer> ids);

    @Query("SELECT ord FROM OutRelationDb ord WHERE ord.id IN " +
            "(SELECT DISTINCT ordb.relationTypeId FROM OutRelationDb ordb WHERE ordb.nodeId = " +
            "(SELECT edb.id FROM EntryDb edb WHERE edb.name = ?1))")
    List<OutRelationDb> findOutRelations(String termToSearch);

    @Query("SELECT ordb.relationTypeId, ordb.outNodeId FROM OutRelationDb ordb WHERE ordb.nodeId = " +
            "(SELECT edb.id FROM EntryDb edb WHERE edb.name = ?1)")
    Map<Integer, Integer> findRelationTypeIdsAndOutNodeIds(String termToSearch);

    @Query("SELECT ordb FROM OutRelationDb ordb WHERE ordb.nodeId = ?1 AND ordb.relationTypeId = ?2")
    List<OutRelationDb> findOutRelationsBySearchedWordIdAndRelationTypeId(int searchedWordId, int relationTypeId);
}
