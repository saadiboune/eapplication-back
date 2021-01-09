package com.eapplication.eapplicationback.models.nodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 * Toutes les Entry (Noeud) dont l'API renvoie (En relation avec le mot recherché)
 */
public class Entry {

    /**
     * Id qu'on avec lequel on peut récupérer (Ex : Chat : 150)
     *      1. les {@link InRelation} via le champ inNodeId
     *      2. les {@link OutRelation} via le champ outNodeId
     */
    private int id;

    /**
     * Nom du noeud (Ex : Chat)
     */
    private String name;
    /**
     * Identifiant de {@link NodeType} champ id
     */
    private int nodeTypeId;

    /**
     * Poids du noeud par rapport au mot recherché (Ex: mot recherché : chat ==> name trouvé loup ==> poind : 600)
     */
    private int weight;

    /**
     * TODO add def
     */
    private String formatedName;

}
