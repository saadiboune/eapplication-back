package com.eapplication.eapplicationback.repository;

import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoComplRepository extends JpaRepository<AutoComplModel, Long> {

    public List<AutoComplModel> findByNode(String node);

    Page<AutoComplModel> findByNodeStartsWith(String node, Pageable pageable);
}
