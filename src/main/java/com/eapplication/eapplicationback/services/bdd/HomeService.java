package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.constantes.Comments;
import com.eapplication.eapplicationback.mapper.*;
import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import com.eapplication.eapplicationback.models.bdd.RelationTypeDb;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.models.ihm.RelationTypeForRaff;
import com.eapplication.eapplicationback.models.nodes.*;
import com.eapplication.eapplicationback.services.AsyncProcessor;
import com.eapplication.eapplicationback.services.AutoComplService;
import com.eapplication.eapplicationback.services.ManageData;
import com.eapplication.eapplicationback.services.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeService {

    RestService restService;

    ManageData manageData;

    AutoComplService autoComplService;

    AsyncProcessor asyncProcessor;

    EntryDbService entryDbService;

    NodeTypeDbService nodeTypeDbService;

    RelationTypeDbService relationTypeDbService;

    OutRelationDbService outRelationDbService;

    InRelationDbService inRelationDbService;

    ModelMapper modelMapper;

    public HomeService(RestService restService, ManageData manageData,
                       AutoComplService autoComplService, AsyncProcessor asyncProcessor,
                       EntryDbService entryDbService, NodeTypeDbService nodeTypeDbService,
                       RelationTypeDbService relationTypeDbService,
                       OutRelationDbService outRelationDbService,
                       InRelationDbService inRelationDbService,
                       ModelMapper modelMapper) {
        this.restService = restService;
        this.manageData = manageData;
        this.autoComplService = autoComplService;
        this.asyncProcessor = asyncProcessor;
        this.entryDbService = entryDbService;
        this.nodeTypeDbService = nodeTypeDbService;
        this.relationTypeDbService = relationTypeDbService;
        this.outRelationDbService = outRelationDbService;
        this.inRelationDbService = inRelationDbService;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<HomeModel> homePage(String termToSearch) throws UnsupportedEncodingException {
        // 1 On intérroge l'API distante avec le mot clé recherché
        String response = restService.callZero(termToSearch);

        if (StringUtils.isEmpty(response)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // On extrait les nodeType de la réponse
        List<NodeType> nodeTypes = this.manageData.constructNodeTypeFromCode(response);

        // On extrait les relationTypes de la réponse
        List<RelationType> relationTypes = this.manageData.constructRelationTypeFromResponse(response);

        // Filtre que sur les raffinement sémantique
        // liste des noms de type de relations (avec relationTypeId, relationTypeName et raffDefinitions initialisé à null)
        List<RelationTypeForRaff> raffListForDefinitions = relationTypes.stream()
                .filter(re -> Comments.RAFF_SEMANTIC.equals(re.getName()))
                .map(rt -> RelationTypeForRaff.builder().relationTypeId(rt.getId()).relationTypeName(rt.getName())
                        .rafDefinitions(new ArrayList<>()).build()).collect(Collectors.toList());

        // On extrait les OutRelations de la réponse
        List<OutRelation> outRelations = this.manageData.constructOutRelationFromCode(response);

        List<InRelation> inRelations = this.manageData.constructInRelationFromCode(response);

        // Récupérer les relations sortantes de chaque type de relation par id (On garde que les poids positifs)
        Map<Integer, List<OutRelation>> positiveOutRelations = outRelations
                .stream().filter(rs -> rs.getWeight() >= 0)
                .collect(Collectors.groupingBy(OutRelation::getRelationTypeId));

        //On extrait les entries de la réponse
        List<Entry> entries = this.manageData.constructEntriesFromResponse(response);

        // On récupère la liste des outNodeId des relation sortantes pour récupérer les Entry avec un id == outNodeId
        // Ensuite : pour chaque Entry, on récupère son formatedName et j'appelle l'API pour récupérer les définitions
        // des noeuds.
        List<Entry> filteredEntries = entries.parallelStream()
                .filter(entry -> StringUtils.startsWith(entry.getFormatedName(), termToSearch + ">"))
                .collect(Collectors.toList());

        Map<String, String> nodeNameAndDefinition = new HashMap<>();

        positiveOutRelations.forEach((id, pOutRelations) -> pOutRelations.forEach(outRelation -> {
            // On filtre sur Entry.id == OutRelation.outNodeId et on fait l'appel avec le formattedName
            // (Ex : chat>mammifère)

            filteredEntries.stream()
                    .filter(entry -> entry.getId() == outRelation.getOutNodeId()).collect(Collectors.toList())
                    .stream()
                    .map(Entry::getFormatedName)
                    .forEach(formatedName -> {
                        try {
                            String r = restService.callZero(formatedName);
                            // Si on a une réponse on extrait le bloc <def> ... </def> pour le rajouter à la liste des
                            // définitions de raffinements
                            if (StringUtils.isNoneEmpty(r)) {
                                String definitions = this.manageData.removeBaliseAndCarriageReturn(
                                        this.manageData.getStringBetweenBalise(
                                                Comments.DEF_START, Comments.DEF_END, r));
                                nodeNameAndDefinition.put(formatedName, definitions);
                                raffListForDefinitions.stream()
                                        .filter(relationTypeForRaff -> relationTypeForRaff.getRelationTypeId() == id)
                                        .findFirst().ifPresentOrElse(
                                        relationTypeForRaff -> relationTypeForRaff.getRafDefinitions().add(definitions),
                                        () -> log.debug("Aucune réponse de l'API pour le formatedName {}", formatedName));
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    });
        }));

        // Récupérer la définition du mot recherché
        String definition = this.manageData.removeBaliseAndCarriageReturn(
                this.manageData.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, response));

        // MAJ de la définition du mot recherché dans la table AutoComplModel
        this.autoComplService.updateAutoComplTable(definition, termToSearch);
        // MAJ des données en async
        this.runAsyncUpdateData(termToSearch, entries, nodeTypes, relationTypes, outRelations,
                inRelations, definition, termToSearch, nodeNameAndDefinition);

        return new ResponseEntity<>(HomeModel.builder()
                .definition(definition)
                .relationTypeWithItsOutRelations(positiveOutRelations)
                .raffSemanticAndOrMorphoDefinitions(raffListForDefinitions).build(), HttpStatus.OK);
    }

    public ResponseEntity<HomeModel> getHomeModelResponseEntity(@PathVariable String wordSearched) {
        log.info("RezoDumpController -> Appel récupérer les données directement depuis la bdd");
        List<RelationTypeForRaff> relationTypeForRaffs = new ArrayList<>();

        this.autoComplService.checkWordSearchedIsUpdateAndExists(wordSearched);

        // Récupérer l'Id du mot recherché
        int searchedWordId = this.entryDbService.getIdByName(wordSearched);
        log.debug("searcheWordId {}", searchedWordId);

        Map<Integer, List<OutRelation>> relationTypeWithItsOutRelations = new HashMap<>();

        // Récupérer les types de relation pour le mot recherché
        List<RelationTypeDb> relationTypeDbs = this.relationTypeDbService.getBySearchedWord(searchedWordId);

        // Parcours des types de relation
        for (RelationTypeDb rtdb : relationTypeDbs) {
            int relationTypeId = rtdb.getId();
            String relationTypeName = rtdb.getName();

            //Récupérer les relations sortantes pour chaque type de relation du mot recherché
            List<OutRelationDb> outRelations = this.outRelationDbService
                    .getOutRelationsByWordSearchedIdAndRelationTypeId(searchedWordId, relationTypeId);
            relationTypeWithItsOutRelations.put(relationTypeId, outRelations.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()));

            List<String> raffDefinitions = new ArrayList<>();

            // Si le type de relation est un raffinement sémantique, on réupère la définition de chaque entrée
            if (Comments.RAFF_SEMANTIC.equals(relationTypeName)) {
                raffDefinitions = this.autoComplService.getRaffSemDefinitions(wordSearched, ":");
            }

            // Si le type de relation est un raffinement morphologique, on réupère la définition de chaque entrée
            if (Comments.RAFF_MORPHO.equals(relationTypeName)) {
                raffDefinitions = this.autoComplService.getRaffMorphoDefinitions(wordSearched, ":");
            }

            /**
             * On construit {@link RelationTypeForRaff}
             */
            relationTypeForRaffs.add(RelationTypeForRaff.builder()
                    .relationTypeId(relationTypeId)
                    .relationTypeName(relationTypeName)
                    .rafDefinitions(raffDefinitions.stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())).build());
        }

        /**
         * On construit {@link HomeModel}
         */
        return new ResponseEntity<>(HomeModel.builder()
                .definition(this.autoComplService.getDefinitionByNode(wordSearched))
                .raffSemanticAndOrMorphoDefinitions(relationTypeForRaffs)
                .relationTypeWithItsOutRelations(relationTypeWithItsOutRelations)
                .build(), HttpStatus.OK);
    }

    /**
     * convertir OuRelationDb to OutRelation
     *
     * @param outRelationDb
     * @return
     */
    private OutRelation convertToDto(OutRelationDb outRelationDb) {
        return modelMapper.map(outRelationDb, OutRelation.class);
    }

    /**
     * @param entries
     * @param inRelations
     * @return
     */
    private void runAsyncUpdateData(String termToSearch, List<Entry> entries, List<NodeType> nodeTypes,
                                    List<RelationType> relationTypes, List<OutRelation> outRelations,
                                    List<InRelation> inRelations, String definition, String node,
                                    Map<String, String> nodeNameAndDefinition) {
        // Update BDD
        try {
            asyncProcessor.asyncTask(updateData(termToSearch, entries, nodeTypes, relationTypes,
                    outRelations, inRelations, definition, node, nodeNameAndDefinition));

        } catch (Exception e) {
            log.error("Une erreur s'est produite lors de la MAJ des données en BDD message : {}", e.getLocalizedMessage(), e);
        }
    }

    /**
     * MAJ des données de la bdd
     *
     * @param termToSearch
     * @param entries
     * @param nodeTypes
     * @param relationTypes
     * @param outRelations
     * @param inRelations
     * @return
     */
    private Callable updateData(String termToSearch, List<Entry> entries, List<NodeType> nodeTypes,
                                List<RelationType> relationTypes, List<OutRelation> outRelations,
                                List<InRelation> inRelations, String definition, String node,
                                Map<String, String> nodeNameAndDefinition) {

        return () -> {
            log.info("Début de la MAJ des données en BDD en Async pour Entry, NodeType, RelationType, " +
                    "OutRelation et InRelation pour le mot cherché {}", termToSearch);

            this.entryDbService.update(EntryMapper.toEntryDb(entries));
            log.debug("fin pour entries");
            this.nodeTypeDbService.update(NodeTypeMapper.toNodeTypeDb(nodeTypes));
            this.relationTypeDbService.update(RelationTypeMapper.toRelationTypeDb(relationTypes));
            this.outRelationDbService.update(OutRelationMapper.toOutRelationDb(outRelations));
            this.inRelationDbService.update(InRelationMapper.toInRelationDb(inRelations));
            nodeNameAndDefinition.forEach((k, v) -> {
                if (this.autoComplService.findBynodeIfExists(k)) {
                    this.autoComplService.updateAutoComplTable(v, k);
                } else this.autoComplService.save(AutoComplModel.builder().node(k).definition(v).build());
            });
            log.info("Fin de la MAJ des données en BDD en Async");
            return null;
        };
    }
}
