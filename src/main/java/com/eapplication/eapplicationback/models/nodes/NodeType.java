package com.eapplication.eapplicationback.models.nodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NodeType {
    /**
     * Identifiant du type de noeud qui est référencé dans {@link Entry} via le champ nodeTypeId
     */
    private int id;

    /**
     * Nom du noeud. Ex : Chat ou Loup pour une recherche Chat
     */
    private String name;
}
