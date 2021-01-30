package com.eapplication.eapplicationback.models.bdd;

import com.eapplication.eapplicationback.models.nodes.Entry;
import com.eapplication.eapplicationback.models.nodes.OutRelation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 * Ex : mot recherché = 'chat' ==> Résultat : {@link Entry} . Lien de {@link } vers ses différentes relations sortantes
 * Tout les types de relations existantes pour un noeud
 */
public class RelationTypeDb {
    /**
     * id : identifiant du type de relation
     *
     * Lien entre {@link OutRelation} via l'attribut (relationTypeId) out_relation
     */
    @Id
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
