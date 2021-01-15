package com.eapplication.eapplicationback.controller;

import com.eapplication.eapplicationback.constantes.Comments;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.models.ihm.RelationTypeForRaff;
import com.eapplication.eapplicationback.models.nodes.Entry;
import com.eapplication.eapplicationback.models.nodes.OutRelation;
import com.eapplication.eapplicationback.models.nodes.RelationType;
import com.eapplication.eapplicationback.services.ManageData;
import com.eapplication.eapplicationback.services.RestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RezoDumpController {

    @Autowired ManageData manageData;

    @Autowired RestService restService;

    /**
     * Get Home informations
     *
     * @param gotermrel word to search
     * @param rel
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api getHeader", content = @Content(schema = @Schema(implementation = String.class))) }) @Operation(summary = "Home page informations", description = "Home page informations", tags = {
            "HomePage" }) @GetMapping("/def-raff") public ResponseEntity<HomeModel> getDefAndRaff(
            @RequestParam final String gotermrel, @RequestParam(required = false) final String rel)
            throws UnsupportedEncodingException {
        // 1 On intérroge l'API distante avec le mot clé recherché
        String response = restService.callZero(gotermrel, rel);
        // On extrait les relationType de la réponse
        List<RelationType> relationType = this.manageData.constructRelationTypeFromCode(
                StringUtils.substringBetween(response, Comments.CODE_START, Comments.CODE_END));

        // liste des noms de type relations
        List<RelationTypeForRaff> raffListForDefinitions = relationType.stream()
                .map(rt -> RelationTypeForRaff.builder().relationTypeId(rt.getId()).relationTypeName(rt.getName())
                        .build()).collect(Collectors.toList());

        // Récupérer les relations sortantes de chaque type de relation par id (On garde que les poids positifs)
        Map<Integer, List<OutRelation>> positiveOutRelations = this.manageData.constructOutRelationFromCode(
                StringUtils.substringBetween(response, Comments.CODE_START, Comments.CODE_END)).stream()
                .filter(rs -> rs.getWeight() >= 0).collect(Collectors.groupingBy(OutRelation::getRelationTypeId));

        // On récupère la liste des outNodeId des relation sortantes pour récupérer les Entry avec un id == outNodeId
        // Ensuite : pour chaque Entry, on récupère son formatedName et j'appelle l'API pour récupérer les définitions
        // des noeuds.
        List<Entry> entries = this.manageData.constructEntriesFromCode(
                StringUtils.substringBetween(response, Comments.CODE_START, Comments.CODE_END))
                .parallelStream().filter(entry -> StringUtils.startsWith(entry.getFormatedName(), gotermrel + ">"))
                .collect(Collectors.toList());

        positiveOutRelations.forEach((id, outRelations) -> outRelations.forEach(outRelation -> {
            // On filtre sur Entry.id == OutRelation.outNodeId et on fait l'appel avec le formattedName
            // (Ex : chat>mammifère)
            entries.stream().filter(entry -> entry.getId() == outRelation.getOutNodeId()).collect(Collectors.toList())
                    .stream().map(Entry::getFormatedName).forEach(formatedName -> {

                try {
                    String r = restService.callZero(formatedName, "");

                    raffListForDefinitions.stream()
                            .filter(relationTypeForRaff -> relationTypeForRaff.getRelationTypeId() == outRelation
                                    .getOutNodeId()).forEach(rForRaff -> {
                        List<String> defToUpdate = rForRaff.getRafdefinitions();
                        defToUpdate.add(this.manageData
                                .getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, r));
                        rForRaff.setRafdefinitions(defToUpdate);

                        System.out.println("Updated : " + rForRaff);
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
        }));

        return new ResponseEntity<>(HomeModel.builder().definition(
                this.manageData.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, response))
                .defRaff(positiveOutRelations).raffSemanticAndOrMorphoDefinitions(raffListForDefinitions).build(),
                HttpStatus.OK);
    }
}
