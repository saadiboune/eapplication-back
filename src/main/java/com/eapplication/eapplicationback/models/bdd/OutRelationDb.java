package com.eapplication.eapplicationback.models.bdd;

import com.eapplication.eapplicationback.models.nodes.Entry;
import com.eapplication.eapplicationback.models.nodes.RelationType;
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
 * Ex : mot recherché = 'chat' ==> Résultat : {@link Entry} . Lien de {@link } vers ses différentes relations sortantes
 * Tout les types de relations existantes pour un noeud
 */
public class OutRelationDb {
    /**
     * Id technique de sauvegarde (Pas d'info fonctionnelle ou reationnelle)
     */
    @Id
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
