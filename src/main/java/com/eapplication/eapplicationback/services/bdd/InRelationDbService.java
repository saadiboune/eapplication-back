package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.EntryDb;
import com.eapplication.eapplicationback.models.bdd.InRelationDb;
import com.eapplication.eapplicationback.repository.InRelationDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InRelationDbService {

    @Autowired
    InRelationDbRepository repository;

    public InRelationDb save(InRelationDb entity) {
        return this.repository.save(entity);
    }

    public void saveAll(List<InRelationDb> inRelationDbList) {
        this.repository.saveAll(inRelationDbList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void update(List<InRelationDb> inRelationDbs){
        log.info("sauvegarde InRelationDb {}", inRelationDbs.size());
        List<InRelationDb> inRelationDbsNotNull = inRelationDbs.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // delete
        this.repository.deleteByIdIn(inRelationDbsNotNull.stream().map(InRelationDb::getId).collect(Collectors.toList()));

        this.repository.saveAll(inRelationDbsNotNull);
    }

}
