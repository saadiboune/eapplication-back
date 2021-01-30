package com.eapplication.eapplicationback.models.nodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 * Ex : mot recherché = 'chat' ==> Résultat : {@link Entry} . Lien de {@link } vers ses différentes relations sortantes
 * Tout les types de relations existantes pour un noeud
 */
public class RelationType {
    /**
     * id : identifiant du type de relation
     *
     * Lien entre {@link OutRelation} via l'attribut (relationTypeId) out_relation
     */
    private int id;

    /**
     * name : nom de type de relation
     */
    private String name;
    /**
     *
     */
    private String trgpName;
    /**
     * help : explications sur le type de relation
     */
    @Column(length = 500)
    private String help;
}
