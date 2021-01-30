package com.eapplication.eapplicationback.mapper;

import com.eapplication.eapplicationback.models.bdd.RelationTypeDb;
import com.eapplication.eapplicationback.models.nodes.RelationType;

import java.util.ArrayList;
import java.util.List;

public class RelationTypeMapper {
    public static List<RelationTypeDb> toRelationTypeDb(List<RelationType> entries){
        List<RelationTypeDb> relationTypeDbList = new ArrayList<>();
        entries.forEach(entry -> relationTypeDbList.add(RelationTypeDb
                .builder()
                .id(entry.getId())
                .name(entry.getName())
                .trgpName(entry.getTrgpName())
                .help(entry.getHelp())
                .build()));
        return relationTypeDbList;
    }
}
