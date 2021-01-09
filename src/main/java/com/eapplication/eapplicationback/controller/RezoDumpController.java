package com.eapplication.eapplicationback.controller;

import com.eapplication.eapplicationback.constantes.Comments;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.models.ihm.RelationTypeForRaff;
import com.eapplication.eapplicationback.models.nodes.Entry;
import com.eapplication.eapplicationback.models.nodes.OutRelation;
import com.eapplication.eapplicationback.models.nodes.RelationType;
import com.eapplication.eapplicationback.services.ManageData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RezoDumpController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ManageData manageData;

    @Value("${rezo.dump.base.url}")
    private String resoDumpBaseUrl;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api getHeader",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @Operation(summary = "Entête et ses vrais attributs", description = "Retourne le header", tags = {"rezzzo"})
    @GetMapping("/rezo-dump")
    public ResponseEntity<List<RelationType>> test(@RequestParam final String gotermrel, @RequestParam(required = false) final String rel) throws URISyntaxException, JsonProcessingException {

        URI uri = new URI(resoDumpBaseUrl);

        //TODO add parameters
        HttpEntity<String> response = call(gotermrel, rel);
        // this.manageData.constructFromString(response.getBody());
        // return new ResponseEntity<>(this.manageData.constructNodeTypeFromCode(StringUtils.substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END)), HttpStatus.OK);

        // return new ResponseEntity<>(this.manageData.constructEntriesFromCode(StringUtils.substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END)), HttpStatus.OK);

        return new ResponseEntity<>(this.manageData.constructRelationTypeFromCode(StringUtils.substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END)), HttpStatus.OK);
        //return new ResponseEntity<>(this.manageData.constructOutRelationFromCode(StringUtils.substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END)), HttpStatus.OK);
        //return new ResponseEntity<>(this.manageData.constructInRelationFromCode(StringUtils.substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END)), HttpStatus.OK);

//        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        //TODO check htt status
//
//        String xmlString =   "<Customer>\r\n" +
//                "  <name>Mary</name>\r\n" +
//                "  <age>37</age>\r\n" +
//                "  <address>\r\n" +
//                "    <street>NANTERRE CT</street>\r\n" +
//                "    <postcode>77471</postcode>\r\n" +
//                "  </address>\r\n" +
//                "</Customer>\r\n";
//
//        coucou(xmlString);
//
//        write2XMLStringggggg(result.getBody());

//Verify request succeed
//        Assert.assertEquals(200, result.getStatusCodeValue());
//        Assert.assertEquals(true, result.getBody().contains("employeeList"));
        // return new ResponseEntity<String>(response.getBody(), HttpStatus.OK);
    }

    // TODO mettre dans les services
    private HttpEntity<String> call(@RequestParam String gotermrel, @RequestParam(required = false) String rel) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.TEXT_HTML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resoDumpBaseUrl)
                .queryParam("gotermsubmit", "Chercher")
                .queryParam("gotermrel", gotermrel)
                .queryParam("rel", rel);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api getHeader",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @Operation(summary = "Entête et ses vrais attributs", description = "Retourne le header", tags = {"rezzzo"})
    @GetMapping("/def-raff")
    public ResponseEntity<HomeModel> getDefAndRaff(@RequestParam final String gotermrel, @RequestParam(required = false) final String rel) {
        HttpEntity<String> response = call(gotermrel, rel);

        List<RelationType> relationType = this.manageData.constructRelationTypeFromCode(StringUtils
                .substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END));

        // liste des noms de type relations
        List<RelationTypeForRaff> raffListForDefinitions = relationType.stream()
                .map(rt -> RelationTypeForRaff.builder()
                .relationTypeId(rt.getId()).relationTypeName(rt.getName()).build()).collect(Collectors.toList());

        // Récupérer les relations sortantes de chaque type de relation par id

        List<OutRelation> outRelations = this.manageData.constructOutRelationFromCode(StringUtils
                .substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END));
        Map<Integer, List<OutRelation>> positiveOutRelations = outRelations.stream()
                .filter(rs -> rs.getWeight() >= 0)
                .collect(Collectors.groupingBy(OutRelation::getRelationTypeId));

        // On récupère la liste des outNodeId des relation sortantes pour récupérer les Entry avec un id == outNodeId
        // Ensuite : pour chaque Entry, je récupère son formatedName et j'appelle l'API pour récupérer les définitions
        // des noeuds.
        List<Entry> entries = this.manageData.constructEntriesFromCode(StringUtils
                .substringBetween(response.getBody(), Comments.CODE_START, Comments.CODE_END));

        // Liste des entry
        List<String> matchDefinitions = new ArrayList<>();
        positiveOutRelations.get(1).forEach(relation -> {
            System.out.println("positiveOutRelations.get(1) ok");
            // On filtre sur Entry.id == OutRelation.outNodeId
            entries.stream().filter(entry -> entry.getId() == relation.getOutNodeId()).collect(Collectors.toList()).stream().map(Entry::getFormatedName).forEach(formatedName -> {
                System.out.println("formatedName : to use " + formatedName);

                try {
                    HttpEntity<String> r = call(URLEncoder.encode("chat>mammifère", StandardCharsets.UTF_8.toString()), "");
                    System.out.println("Response call : " + r);

                    raffListForDefinitions.stream().filter(relationTypeForRaff -> relationTypeForRaff.getRelationTypeId() == relation.getOutNodeId()).forEach(rForRaff -> {
                        List<String> defToUpdate = rForRaff.getRafdefinitions();
                        defToUpdate.add(this.manageData.getStringBetweenBalise(Comments.DEF_START,
                                Comments.DEF_END, r.getBody()));
                        rForRaff.setRafdefinitions(defToUpdate);

                        System.out.println("Updated : " + rForRaff);
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            });
        });

        return new ResponseEntity<>(HomeModel.builder().definition(this.manageData.getStringBetweenBalise(Comments.DEF_START,
                Comments.DEF_END, response.getBody())).defRaff(positiveOutRelations).raffSemanticAndOrMorphoDefinitions(raffListForDefinitions).build(), HttpStatus.OK);
    }

    /*
     * Convert Object to XML String
     */
    public static String write2XMLString(Object object)
            throws JsonProcessingException {

        XmlMapper xmlMapper = new XmlMapper();
        // use the line of code for pretty-print XML on console. We should remove it in production.
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return xmlMapper.writeValueAsString(object);
    }

    public static String write2XMLStringggggg(String jsonString)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper xmlMapper = new XmlMapper();

        //JsonNode tree = objectMapper.readTree(jsonString);
        JsonNode jsonAsXml = xmlMapper.readTree(jsonString); //writer().withRootName("RootTagName").writeValueAsString(tree);

        return "";
    }

    private static void coucou(String xml) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        Object poppy = xmlMapper.readValue(xml, Object.class);

        String test = "";

    }
}
