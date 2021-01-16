package com.eapplication.eapplicationback.controller;

import com.eapplication.eapplicationback.constantes.Comments;
import com.eapplication.eapplicationback.mapper.EntryMapper;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.models.ihm.RelationTypeForRaff;
import com.eapplication.eapplicationback.models.nodes.Entry;
import com.eapplication.eapplicationback.models.nodes.OutRelation;
import com.eapplication.eapplicationback.models.nodes.RelationType;
import com.eapplication.eapplicationback.services.AsyncProcessor;
import com.eapplication.eapplicationback.services.ManageData;
import com.eapplication.eapplicationback.services.RestService;
import com.eapplication.eapplicationback.services.bdd.EntryDbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
public class RezoDumpController {

	ManageData manageData;

	RestService restService;

	EntryDbService service;

	private AsyncProcessor asyncProcessor;

	public RezoDumpController(EntryDbService service, ManageData manageData, RestService restService, AsyncProcessor asyncProcessor) {
		this.manageData = manageData;
		this.restService = restService;
		this.asyncProcessor = asyncProcessor;
		this.service = service;
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

		// On extrait les relationType de la réponse
		List<RelationType> relationType = this.manageData.constructRelationTypeFromResponse(response);

		// Filtre que sur les raffinement sémantique
		// liste des noms de type relations (avec relationTypeId, relationTypeName et raffDefinitions initialisé à null)
		List<RelationTypeForRaff> raffListForDefinitions = relationType.stream()
				.filter(re -> Comments.RAFF_SEMANTIC.equals(re.getName()))
				.map(rt -> RelationTypeForRaff.builder().relationTypeId(rt.getId()).relationTypeName(rt.getName())
						.rafDefinitions(new ArrayList<>()).build()).collect(Collectors.toList());

		// Récupérer les relations sortantes de chaque type de relation par id (On garde que les poids positifs)
		Map<Integer, List<OutRelation>> positiveOutRelations = this.manageData.constructOutRelationFromCode(response)
				.stream().filter(rs -> rs.getWeight() >= 0)
				.collect(Collectors.groupingBy(OutRelation::getRelationTypeId));

		List<Entry> entries = this.manageData.constructEntriesFromResponse(response);

		// On récupère la liste des outNodeId des relation sortantes pour récupérer les Entry avec un id == outNodeId
		// Ensuite : pour chaque Entry, on récupère son formatedName et j'appelle l'API pour récupérer les définitions
		// des noeuds.
		List<Entry> filteredEntries = entries.parallelStream()
				.filter(entry -> StringUtils.startsWith(entry.getFormatedName(), termToSearch + ">"))
				.collect(Collectors.toList());

		positiveOutRelations.forEach((id, outRelations) -> outRelations.forEach(outRelation -> {
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

		this.runAssync(entries);

		return new ResponseEntity<>(HomeModel.builder()
				.definition(this.manageData.getStringBetweenBalise(Comments.DEF_START, Comments.DEF_END, response))
				.relationTypeWithItsOutRelations(positiveOutRelations)
				.raffSemanticAndOrMorphoDefinitions(raffListForDefinitions).build(), HttpStatus.OK);
	}

	/**
	 *
	 * @param entries
	 * @return
	 */
	private void runAssync(List<Entry> entries){
		// Update BDD
		try {
			asyncProcessor.asyncTask(updateData(entries));

		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
		}
	}

	private Callable updateData(List<Entry> entries) {

		return () -> {
			System.out.println("En assync j'attend du coup ");

			System.out.println("En assync et j'ai fini ");
			this.service.saveAll(EntryMapper.toEntryDb(entries));
			return entries;
		};
	}
}
