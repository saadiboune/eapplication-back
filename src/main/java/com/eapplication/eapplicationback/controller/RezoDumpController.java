package com.eapplication.eapplicationback.controller;

import com.eapplication.eapplicationback.constantes.Comments;
import com.eapplication.eapplicationback.mapper.*;
import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import com.eapplication.eapplicationback.models.dto.AutoCompDTO;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.models.ihm.RelationTypeForRaff;
import com.eapplication.eapplicationback.models.nodes.*;
import com.eapplication.eapplicationback.repository.OutRelationDbRepository;
import com.eapplication.eapplicationback.services.AsyncProcessor;
import com.eapplication.eapplicationback.services.AutoComplService;
import com.eapplication.eapplicationback.services.ManageData;
import com.eapplication.eapplicationback.services.RestService;
import com.eapplication.eapplicationback.services.bdd.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class RezoDumpController {

	ManageData manageData;

	RestService restService;

	EntryDbService entryDbService;

	NodeTypeDbService nodeTypeDbService;

	RelationTypeDbService relationTypeDbService;

	OutRelationDbService outRelationDbService;

	InRelationDbService inRelationDbService;

	AutoComplService autoComplService;

//	private AsyncProcessor asyncProcessor;

	public RezoDumpController(EntryDbService entryDbService, ManageData manageData, RestService restService/*,
							  AsyncProcessor asyncProcessor*/, NodeTypeDbService nodeTypeDbService,
							  RelationTypeDbService relationTypeDbService,
							  OutRelationDbService outRelationDbService,
							  InRelationDbService inRelationDbService,
	AutoComplService autoComplService) {
		this.manageData = manageData;
		this.restService = restService;
//		this.asyncProcessor = asyncProcessor;
		this.entryDbService = entryDbService;
		this.nodeTypeDbService = nodeTypeDbService;
		this.relationTypeDbService = relationTypeDbService;
		this.outRelationDbService = outRelationDbService;
		this.inRelationDbService = inRelationDbService;
		this.autoComplService = autoComplService;
	}

	/**
	 * Get Home informations
	 *
	 * @param termToSearch word to search
	 * @return
	 */
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Api getHeader", content = @Content(schema = @Schema(implementation = String.class))) }) @Operation(summary = "Home page informations", description = "Home page informations", tags = {
			"HomePage" }) @GetMapping("/def-raff") public ResponseEntity<HomeModel> homePage(
			@RequestParam final String termToSearch) throws UnsupportedEncodingException {
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
		// liste des noms de type relations (avec relationTypeId, relationTypeName et raffDefinitions initialisé à null)
		List<RelationTypeForRaff> raffListForDefinitions = relationTypes.stream()
				.filter(re -> Comments.RAFF_SEMANTIC.equals(re.getName()))
				.map(rt -> RelationTypeForRaff.builder().relationTypeId(rt.getId()).relationTypeName(rt.getName())
						.rafDefinitions(new ArrayList<>()).build()).collect(Collectors.toList());

		// On extrait les outRelation de la réponse
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

		positiveOutRelations.forEach((id, pOutRelations) -> pOutRelations.forEach(outRelation -> {
			// On filtre sur Entry.id == OutRelation.outNodeId et on fait l'appel avec le formattedName
			// (Ex : chat>mammifère)

			filteredEntries.stream().filter(entry -> entry.getId() == outRelation.getOutNodeId()).collect(Collectors.toList())
					.stream().map(Entry::getFormatedName).forEach(formatedName -> {

				try {
					String r = restService.callZero(formatedName);
					// Si on a une réponse on extrait le bloc <def> ... </def> pour le rajouter à la liste des
					// définition du raffinement
					if (StringUtils.isNoneEmpty(r)) {
						raffListForDefinitions.stream()
								.filter(relationTypeForRaff -> relationTypeForRaff.getRelationTypeId() == id)
								.findFirst().ifPresentOrElse(
								relationTypeForRaff -> relationTypeForRaff.getRafDefinitions().add(this.manageData
										.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, r)),
								() -> System.out.println("rien à rajouter"));
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			});
		}));

//		this.runAssync(entries, nodeTypes, relationTypes, outRelations, inRelations);


//		this.inRelationDbService.saveAll(InRelationMapper.toInRelationDb(inRelations));
//		log.info("sauvegarde inRelation");
		this.entryDbService.saveAll(EntryMapper.toEntryDb(entries));
		log.info("sauvegarde entries");
		this.nodeTypeDbService.saveAll(NodeTypeMapper.toNodeTypeDb(nodeTypes));
		log.info("sauvegarde nodeTypes");
		this.relationTypeDbService.saveAll(RelationTypeMapper.toRelationTypeDb(relationTypes));
		log.info("sauvegarde relationType");
		this.outRelationDbService.saveAll(OutRelationMapper.toOutRelationDb(outRelations));
		log.info("sauvegarde outRelation");

		return new ResponseEntity<>(HomeModel.builder()
				.definition(this.manageData.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, response))
				.relationTypeWithItsOutRelations(positiveOutRelations)
				.raffSemanticAndOrMorphoDefinitions(raffListForDefinitions).build(), HttpStatus.OK);
	}

//	/**
//	 *
//	 * @param entries
//	 * @param inRelations
//	 * @return
//	 */
//	private void runAssync(List<Entry> entries, List<NodeType> nodeTypes, List<RelationType> relationTypes,
//						   List<OutRelation> outRelations, List<InRelation> inRelations) {
//		// Update BDD
//		try {
//			asyncProcessor.asyncTask(updateData(entries, nodeTypes, relationTypes, outRelations, inRelations));
//
//		} catch (Exception e) {
//			System.out.println("error" + e.getMessage());
//		}
//	}

//	private Callable updateData(List<Entry> entries, List<NodeType> nodeTypes, List<RelationType> relationTypes,
//								List<OutRelation> outRelations, List<InRelation> inRelations) {
//
//		return () -> {
//			System.out.println("En assync j'attend du coup ");
//
//			System.out.println("En assync et j'ai fini ");
//			this.entryDbService.saveAll(EntryMapper.toEntryDb(entries));
//			log.info("sauvegarde entries");
//			this.nodeTypeDbService.saveAll(NodeTypeMapper.toNodeTypeDb(nodeTypes));
//			log.info("sauvegarde nodeTypes");
//			this.relationTypeDbService.saveAll(RelationTypeMapper.toRelationTypeDb(relationTypes));
//			log.info("sauvegarde relationType");
//			this.outRelationDbService.saveAll(OutRelationMapper.toOutRelationDb(outRelations));
//			log.info("sauvegarde outRelation");
//			this.inRelationDbService.saveAll(InRelationMapper.toInRelationDb(inRelations));
//			log.info("sauvegarde inRelation");
//			return outRelations;
//		};
//	}



	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Api findAutoCompl", content = @Content(schema = @Schema(implementation = AutoComplModel.class))) })
	@Operation(summary = "Auto-completion", description = "Retourne les valeurs possibles d'un filtre", tags = {
			"Filters" })
	@PostMapping(value = "autocomplete", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<AutoComplModel>> findAutoCompl(@RequestParam String node,
													 @RequestParam int size, @RequestParam int page) {
		log.info("RezoDumpController -> Appel WS getAutocomplete");

		return new ResponseEntity<>(autoComplService.findByNodeStartsWith(node, page, size), HttpStatus.OK);
	}

}
