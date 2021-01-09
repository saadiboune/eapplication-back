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
 * Model des relations entrantes pour le noeud (mot) recherché
 */
public class InRelation {

    /**
     * Id technique de sauvegarde (Pas d'info fonctionnelle ou reationnelle)
     */
    private int id;

    /**
     * Id de noeud référencé dans {@link Entry} via le champ id (Ex les relations entrante vers Chat)
     */
    private int inNodeId;
    /**
     * Id de noeud recherché (Ex : Chat : id 150) référencé dans {@link Entry} via le champ id
     */
    private int nodeId;

    /**
     * Identifiant de {@link RelationType}
     */
    private int relationTypeId;

    /**
     * Poids de l'association entre inNodeId et le nodeId (Noeud saisi ou recherché)
     */
    private int weight;
}
