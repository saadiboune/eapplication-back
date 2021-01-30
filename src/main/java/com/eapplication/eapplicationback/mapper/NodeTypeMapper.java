package com.eapplication.eapplicationback.mapper;

import com.eapplication.eapplicationback.models.bdd.NodeTypeDb;
import com.eapplication.eapplicationback.models.nodes.NodeType;

import java.util.ArrayList;
import java.util.List;

public class NodeTypeMapper {
    public static List<NodeTypeDb> toNodeTypeDb(List<NodeType> entries){
        List<NodeTypeDb> nodeTypeDbList = new ArrayList<>();
        entries.forEach(entry -> nodeTypeDbList.add(NodeTypeDb.builder()
                .id(entry.getId())
                .name(entry.getName())
                .build()));
        return nodeTypeDbList;
    }
}
