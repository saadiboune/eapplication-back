package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AutoComplRepository extends JpaRepository<AutoComplModel, Long> {

    Page<AutoComplModel> findByNodeStartsWith(String node, Pageable pageable);

    void deleteByIdIn(List<Integer> ids);

    @Transactional
    @Modifying
    @Query("UPDATE AutoComplModel ac SET ac.lastConsultDate = current_time , ac.definition = ?1 WHERE ac.node = ?2")
    void update(String definition, String node);

    List<AutoComplModel> findByNode(String node);

    @Query("SELECT acm.definition from AutoComplModel acm where acm.node = ?1")
    String findDefinitionByNode(String node);

    @Query("SELECT acm.definition from AutoComplModel acm where acm.node like ?1%")
    List<String> findDefinitions(String termToSearch);

    List<AutoComplModel> findByNodeStartsWithAndNodeNotContains(String searchedWord, String sep);

    List<AutoComplModel> findByNodeStartsWithAndNodeContains(String searchedWord, String sep);

}
