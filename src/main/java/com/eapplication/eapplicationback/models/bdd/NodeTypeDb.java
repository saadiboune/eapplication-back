package com.eapplication.eapplicationback.models.bdd;

import com.eapplication.eapplicationback.models.nodes.Entry;
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
public class NodeTypeDb {
    /**
     * Identifiant du type de noeud qui est référencé dans {@link Entry} via le champ nodeTypeId
     */
    @Id
    private int id;

    /**
     * Nom du noeud. Ex : Chat ou Loup pour une recherche Chat
     */
    private String name;
}
