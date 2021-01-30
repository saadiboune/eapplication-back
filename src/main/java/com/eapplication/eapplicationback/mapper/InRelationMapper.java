package com.eapplication.eapplicationback.mapper;

import com.eapplication.eapplicationback.models.bdd.InRelationDb;
import com.eapplication.eapplicationback.models.nodes.InRelation;

import java.util.ArrayList;
import java.util.List;

public class InRelationMapper {
    public static List<InRelationDb> toInRelationDb(List<InRelation> entries){
        List<InRelationDb> inRelationDbList = new ArrayList<>();
        entries.forEach(entry -> inRelationDbList.add(InRelationDb.builder()
                .id(entry.getId())
                .inNodeId(entry.getInNodeId())
                .nodeId(entry.getNodeId())
                .relationTypeId(entry.getRelationTypeId())
                .weight(entry.getWeight())
                .build()));
        return inRelationDbList;
    }
}
