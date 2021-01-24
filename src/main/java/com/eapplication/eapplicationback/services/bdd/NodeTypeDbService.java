package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.InRelationDb;
import com.eapplication.eapplicationback.models.bdd.NodeTypeDb;
import com.eapplication.eapplicationback.repository.NodeTypeDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NodeTypeDbService {

    @Autowired
    NodeTypeDbRepository repository;

    public NodeTypeDb save(NodeTypeDb entity) {
        return this.repository.save(entity);
    }

    public void saveAll(List<NodeTypeDb> nodeTypeDbList) {
        this.repository.saveAll(nodeTypeDbList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void update(List<NodeTypeDb> nodeTypeDbList){
        log.info("sauvegarde NodeTypeDb {}", nodeTypeDbList.size());
        List<NodeTypeDb> nodeTypeDbListNotNull = nodeTypeDbList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // delete
        this.repository.deleteByIdIn(nodeTypeDbListNotNull.stream().map(NodeTypeDb::getId).collect(Collectors.toList()));

        this.repository.saveAll(nodeTypeDbListNotNull);
    }
}
