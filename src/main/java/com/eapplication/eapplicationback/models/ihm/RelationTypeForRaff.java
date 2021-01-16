package com.eapplication.eapplicationback.models.ihm;
import com.eapplication.eapplicationback.models.nodes.RelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.eapplication.eapplicationback.models.nodes.OutRelation;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 * Model de définitions de tous les noeuds en lien avec le noeud saisi ayant un raffinement sémantique et/ou
 * morphologique
 */
public class RelationTypeForRaff {
    /**
     * identifiant du type de relation : attribut id de {@link RelationType}
     */
    private int relationTypeId;
    /**
     * nom du type de relation : attribut name de {@link RelationType}
     */
    private String relationTypeName;

    /**
     * Liste utilisée pour l'IHM
     *
     * Définitions des raffinnements (noeuds sortants) {@link OutRelation}.outNodeId en relation avec le noeud saisi
     */
    private List<String> rafDefinitions;
}
