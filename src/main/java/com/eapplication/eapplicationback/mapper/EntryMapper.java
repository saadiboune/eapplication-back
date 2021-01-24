package com.eapplication.eapplicationback.mapper;

import com.eapplication.eapplicationback.models.bdd.EntryDb;
import com.eapplication.eapplicationback.models.nodes.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryMapper {
    public static List<EntryDb> toEntryDb(List<Entry> entries) {
        List<EntryDb> entryDbList = new ArrayList<>();
        entries.forEach(entry -> entryDbList.add(EntryDb.builder()
                .id(entry.getId())
                .name(entry.getName())
                .formatedName(entry.getFormatedName())
                .nodeTypeId(entry.getNodeTypeId())
                .weight(entry.getWeight())
                .build()));
        return entryDbList;
    }
}
