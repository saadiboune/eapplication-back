package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.NodeTypeDb;
import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import com.eapplication.eapplicationback.repository.OutRelationDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OutRelationDbService {

    @Autowired
    OutRelationDbRepository repository;

    public OutRelationDb save(OutRelationDb entity) {
        return this.repository.save(entity);
    }

    public void saveAll(List<OutRelationDb> outRelationDbList) {
        this.repository.saveAll(outRelationDbList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void update(List<OutRelationDb> outRelationDbs){
        log.info("sauvegarde OutRelationDb {}", outRelationDbs.size());
        List<OutRelationDb> outRelationDbsNotNull = outRelationDbs.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // delete
        this.repository.deleteByIdIn(outRelationDbsNotNull.stream().map(OutRelationDb::getId).collect(Collectors.toList()));

        this.repository.saveAll(outRelationDbsNotNull);
    }

    public Map<Integer, Integer> getRelationTypeIdsAndOutNodeIds(String termTosearch) {
        return this.repository.findRelationTypeIdsAndOutNodeIds(termTosearch);
    }

    public List<OutRelationDb> getOutRelationsByWordSearchedIdAndRelationTypeId(int searchedWordId, int relationTypeId) {
        return this.repository.findOutRelationsBySearchedWordIdAndRelationTypeId(searchedWordId, relationTypeId);
    }

}
