package com.eapplication.eapplicationback.controller;

import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import com.eapplication.eapplicationback.models.bdd.EntryDb;
import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.models.nodes.OutRelation;
import com.eapplication.eapplicationback.services.AutoComplService;
import com.eapplication.eapplicationback.services.bdd.EntryDbService;
import com.eapplication.eapplicationback.services.bdd.OutRelationDbService;
import com.eapplication.eapplicationback.services.bdd.RelationTypeDbService;
import com.eapplication.eapplicationback.services.bdd.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/jeuxdemots")
@Slf4j
public class RezoDumpController {

    EntryDbService entryDbService;

    RelationTypeDbService relationTypeDbService;

    OutRelationDbService outRelationDbService;

    AutoComplService autoComplService;

    HomeService homeService;

    public RezoDumpController(EntryDbService entryDbService,
                              RelationTypeDbService relationTypeDbService,
                              OutRelationDbService outRelationDbService,
                              AutoComplService autoComplService,
                              HomeService homeService) {
        this.entryDbService = entryDbService;
        this.relationTypeDbService = relationTypeDbService;
        this.outRelationDbService = outRelationDbService;
        this.autoComplService = autoComplService;
        this.homeService = homeService;
    }

//    /**
//     * Get Home informations
//     *
//     * @param termToSearch word to search
//     * @return
//     */
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Api getHeader",
//                    content = @Content(schema = @Schema(implementation = String.class)))})
//    @Operation(summary = "Home page informations", description = "Home page informations", tags = {
//            "HomePage"})
//    @GetMapping("/def-raff")
//    public ResponseEntity<HomeModel> homePage(
//            @RequestParam final String termToSearch) throws UnsupportedEncodingException {
//        // 1 On intérroge l'API distante avec le mot clé recherché
//        String response = restService.callZero(termToSearch);
//
//        if (StringUtils.isEmpty(response)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        // On extrait les nodeType de la réponse
//        List<NodeType> nodeTypes = this.manageData.constructNodeTypeFromCode(response);
//
//        // On extrait les relationTypes de la réponse
//        List<RelationType> relationTypes = this.manageData.constructRelationTypeFromResponse(response);
//
//        // Filtre que sur les raffinement sémantique
//        // liste des noms de type de relations (avec relationTypeId, relationTypeName et raffDefinitions initialisé à null)
//        List<RelationTypeForRaff> raffListForDefinitions = relationTypes.stream()
//                .filter(re -> Comments.RAFF_SEMANTIC.equals(re.getName()))
//                .map(rt -> RelationTypeForRaff.builder().relationTypeId(rt.getId()).relationTypeName(rt.getName())
//                        .rafDefinitions(new ArrayList<>()).build()).collect(Collectors.toList());
//        // TODO insert or update every definition in AutoComplTable
//
//        // On extrait les OutRelations de la réponse
//        List<OutRelation> outRelations = this.manageData.constructOutRelationFromCode(response);
//
//        List<InRelation> inRelations = this.manageData.constructInRelationFromCode(response);
//
//        // Récupérer les relations sortantes de chaque type de relation par id (On garde que les poids positifs)
//        Map<Integer, List<OutRelation>> positiveOutRelations = outRelations
//                .stream().filter(rs -> rs.getWeight() >= 0)
//                .collect(Collectors.groupingBy(OutRelation::getRelationTypeId));
//
//        //On extrait les entries de la réponse
//        List<Entry> entries = this.manageData.constructEntriesFromResponse(response);
//
//        // On récupère la liste des outNodeId des relation sortantes pour récupérer les Entry avec un id == outNodeId
//        // Ensuite : pour chaque Entry, on récupère son formatedName et j'appelle l'API pour récupérer les définitions
//        // des noeuds.
//        List<Entry> filteredEntries = entries.parallelStream()
//                .filter(entry -> StringUtils.startsWith(entry.getFormatedName(), termToSearch + ">"))
//                .collect(Collectors.toList());
//
//        Map<String, String> nodeNameAndDefinition = new HashMap<>();
//
//        positiveOutRelations.forEach((id, pOutRelations) -> pOutRelations.forEach(outRelation -> {
//            // On filtre sur Entry.id == OutRelation.outNodeId et on fait l'appel avec le formattedName
//            // (Ex : chat>mammifère)
//
//            filteredEntries.stream()
//                    .filter(entry -> entry.getId() == outRelation.getOutNodeId()).collect(Collectors.toList())
//                    .stream()
//                    .map(Entry::getFormatedName)
//                    .forEach(formatedName -> {
//                        try {
//                            String r = restService.callZero(formatedName);
//                            // Si on a une réponse on extrait le bloc <def> ... </def> pour le rajouter à la liste des
//                            // définitions de raffinements
//                            if (StringUtils.isNoneEmpty(r)) {
//                                String definitions = this.manageData.removeBaliseAndCarriageReturn(this.manageData.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, r));
//                                nodeNameAndDefinition.put(formatedName, definitions);
//                                raffListForDefinitions.stream()
//                                        .filter(relationTypeForRaff -> relationTypeForRaff.getRelationTypeId() == id)
//                                        .findFirst().ifPresentOrElse(
//                                        relationTypeForRaff -> relationTypeForRaff.getRafDefinitions().add(definitions),
//                                        () -> log.debug("Aucune réponse de l'API pour le formatedName {}", formatedName));
//                            }
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    });
//        }));
////        if (this.autoComplService.findBynodeIfExists("saadi")) {
////            System.out.println("coucou : " + this.autoComplService.findBynodeIfExists("chat"));
////        }
////        nodeNameAndDefinition.forEach((k, v) -> {
////            this.autoComplService.updateAutoComplTable(v, k);
////        });
//
//        // Récupérer la définition du mot recherché
//        String definition = this.manageData.removeBaliseAndCarriageReturn(
//                this.manageData.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, response));
//
//        // MAJ de la définition du mot recherché dans la table AutoComplModel
//        this.autoComplService.updateAutoComplTable(definition, termToSearch);
//        // MAJ des données en async
//        this.runAsyncUpdateData(termToSearch, entries, nodeTypes, relationTypes, outRelations,
//                inRelations, definition, termToSearch, nodeNameAndDefinition);
//
//        return new ResponseEntity<>(HomeModel.builder()
//                .definition(definition)
//                .relationTypeWithItsOutRelations(positiveOutRelations)
//                .raffSemanticAndOrMorphoDefinitions(raffListForDefinitions).build(), HttpStatus.OK);
//    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api findAutoCompl",
                    content = @Content(schema = @Schema(implementation = AutoComplModel.class)))})
    @Operation(summary = "Auto-completion", description = "Retourne les valeurs possibles d'un filtre",
            tags = {"Filters"})
    @PostMapping(value = "autocomplete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AutoComplModel>> findAutoCompl(@RequestParam String node,
                                                              @RequestParam int size, @RequestParam int page) {
        log.info("RezoDumpController -> Appel WS getAutocomplete");

        return new ResponseEntity<>(autoComplService.findByNodeStartsWith(node, page, size), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api findNodesRelatedToSpecificRelationType",
                    content = @Content(schema = @Schema(implementation = EntryDb.class)))})
    @Operation(summary = "Recherche d'information pour un type de relation",
            description = "Retourne les noeuds associés à un type de relation", tags = {"Filters"})
    @GetMapping("/details")
    public ResponseEntity<Page<EntryDb>> findNodesRelatedToSpecificRelationType(
            @RequestParam int relationTypeId,
            @RequestParam int nodeIdSearched,
            @RequestParam int size,
            @RequestParam int page) {
        log.info("RezoDumpController -> Appel WS findNodesRelatedToSpecificRelationType");

        return new ResponseEntity<>(entryDbService.findEntriesRelatedToSpecificRelationType(
                relationTypeId, nodeIdSearched, page, size), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api findNodesRelatedToSpecificRelationType",
                    content = @Content(schema = @Schema(implementation = EntryDb.class)))})
    @Operation(summary = "Recherche d'information pour un type de relation",
            description = "Retourne les noeuds associés à un type de relation", tags = {"Filters"})
    @GetMapping("/{wordSearched}")
    @Cacheable("donnees")
    public ResponseEntity<HomeModel> homePageFromBdd(@PathVariable String wordSearched) throws UnsupportedEncodingException {

        if (!this.autoComplService.checkWordSearchedIsUpdateAndExists(wordSearched)) {
            log.info("RezoDumpController -> Appel UpdateBDService pour récupérer les données de la page d'accueil et mettre à jour la bdd");
            return this.homeService.homePage(wordSearched);
        }
        return this.homeService.getHomeModelResponseEntity(wordSearched);
    }
}
