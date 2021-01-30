package com.eapplication.eapplicationback.models.ihm;

import com.eapplication.eapplicationback.models.nodes.OutRelation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 * Model utilisé pour aggréger les informations à afficher dans l'IHM
 */
public class HomeModel {
    /**
     * Définition du noeud saisi
     */
    private String definition;

    /**
     * liste des définitions de tous les noeuds en lien avec le noeud saisi ayant un raffinement sémantique et/ou
     * morphologique
     */
    private List<RelationTypeForRaff> raffSemanticAndOrMorphoDefinitions;

    /**
     * Map qui a pour clé l'identifiant du type de relation et la valeur est une liste de relations sortantes
     */
    private Map<Integer, List<OutRelation>> relationTypeWithItsOutRelations;
}
