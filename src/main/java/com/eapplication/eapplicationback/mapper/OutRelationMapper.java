package com.eapplication.eapplicationback.mapper;

import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import com.eapplication.eapplicationback.models.nodes.OutRelation;

import java.util.ArrayList;
import java.util.List;

public class OutRelationMapper {
    public static List<OutRelationDb> toOutRelationDb(List<OutRelation> outRelations){
        List<OutRelationDb> outRelationDbList = new ArrayList<>();
        outRelations.forEach(entry -> outRelationDbList.add(OutRelationDb.builder()
                .id(entry.getId())
                .nodeId(entry.getNodeId())
                .outNodeId(entry.getOutNodeId())
                .relationTypeId(entry.getRelationTypeId())
                .weight(entry.getWeight())
                .build()));
        return outRelationDbList;
    }
}
