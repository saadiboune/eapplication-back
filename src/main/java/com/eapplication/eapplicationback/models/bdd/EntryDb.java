package com.eapplication.eapplicationback.models.bdd;

import com.eapplication.eapplicationback.models.nodes.InRelation;
import com.eapplication.eapplicationback.models.nodes.NodeType;
import com.eapplication.eapplicationback.models.nodes.OutRelation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 * Toutes les Entry (Noeud) dont l'API renvoie (En relation avec le mot recherché)
 */
public class EntryDb {

    /**
     * Id qu'on avec lequel on peut récupérer (Ex : Chat : 150)
     *      1. les {@link InRelation} via le champ inNodeId
     *      2. les {@link OutRelation} via le champ outNodeId
     */
    @Id
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
