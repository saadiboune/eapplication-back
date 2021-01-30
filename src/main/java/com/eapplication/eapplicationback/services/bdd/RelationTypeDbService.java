package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.RelationTypeDb;
import com.eapplication.eapplicationback.repository.RelationTypeDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RelationTypeDbService {

    @Autowired
    RelationTypeDbRepository repository;

    public RelationTypeDb save(RelationTypeDb entity) {
        return this.repository.save(entity);
    }

    public void saveAll(List<RelationTypeDb> relationTypeDbList) {
        this.repository.saveAll(relationTypeDbList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void update(List<RelationTypeDb> relationTypeDbs){
        log.info("sauvegarde RelationTypeDb {}", relationTypeDbs.size());
        List<RelationTypeDb> relationTypeDbstNotNull = relationTypeDbs.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // delete
        this.repository.deleteByIdIn(relationTypeDbstNotNull.stream().map(RelationTypeDb::getId).collect(Collectors.toList()));

        this.repository.saveAll(relationTypeDbstNotNull);
    }

    public int getIdByName(String name) {
        return this.repository.findIdByName(name);
    }

    public String getRelationTypeName(int id) {
        return this.repository.findNameById(id);
    }

    public List<RelationTypeDb> getBySearchedWord(int searchedWordId) {
        return this.repository.getBySearchedWord(searchedWordId);
    }

}
