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
 * Model des relations sortantes pour le noeud (mot) recherché
 */
public class OutRelation {
    /**
     * Id technique de sauvegarde (Pas d'info fonctionnelle ou reationnelle)
     */
    private int id;

    /**
     * Id de noeud recherché (Ex : Chat : id 150) référencé dans {@link Entry} via le champ id
     */
    private int nodeId;

    /**
     * Id de noeud référencé dans {@link Entry} via le champ id
     */
    private int outNodeId;
    /**
     * Identifiant de la la {@link RelationType}
     */
    private int relationTypeId;

    /**
     * Poids de l'association entre outNodeId et le nodeId (Noeud saisi ou recherché)
     */
    private int weight;
}
