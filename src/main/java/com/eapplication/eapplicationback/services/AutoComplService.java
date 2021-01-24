package com.eapplication.eapplicationback.services;

import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import com.eapplication.eapplicationback.repository.AutoComplRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AutoComplService {

    private AutoComplRepository autoComplRepository;

    public AutoComplService(AutoComplRepository autoComplRepository) {
        this.autoComplRepository = autoComplRepository;
    }

    public void saveAll(List<AutoComplModel> autoComplModels) {
        autoComplRepository.saveAll(autoComplModels);
    }

    /**
     * delete all
     */
    public void deleteAll() {
        autoComplRepository.deleteAll();
    }

    public long count() {
        return autoComplRepository.count();
    }

    public Page<AutoComplModel> findByNodeStartsWith(String node, int page, int size) {
        return autoComplRepository.findByNodeStartsWith(node, PageRequest.of(page, size));
    }

    public void updateAutoComplTable(String definition, String node) {
        log.info("Mise à jour autoComplTable noeud {} avec la definition {}", node, definition);
        this.autoComplRepository.update(definition, node);
    }

    public boolean findBynodeIfExists(String node) {
        return (this.autoComplRepository.findByNode(node) != null);
    }

    public void save(AutoComplModel autoComplModel) {
        this.autoComplRepository.save(autoComplModel);
    }

    public String getDefinitionByNode(String node) {
        return this.autoComplRepository.findDefinitionByNode(node);
    }

    public List<String> getDefinitions(String termToSearch) {
        return this.autoComplRepository.findDefinitions(termToSearch);
    }

    public List<String> getRaffSemDefinitions(String searchedWord, String separator) {
        return this.autoComplRepository.findByNodeStartsWithAndNodeNotContains(searchedWord, separator)
                .stream().map(AutoComplModel::getDefinition).collect(Collectors.toList());
    }

    public List<String> getRaffMorphoDefinitions(String searchedWord, String separator) {
        return this.autoComplRepository.findByNodeStartsWithAndNodeContains(searchedWord, separator)
                .stream().map(AutoComplModel::getDefinition).collect(Collectors.toList());
    }

    public boolean checkWordSearchedIsUpdateAndExists(String searchedWord) {
        List<AutoComplModel> autoComplModel = this.autoComplRepository.findByNode(searchedWord);
        return autoComplModel.stream().findFirst().isPresent() &&
                autoComplModel.stream().findFirst().get().getLastConsultDate() != null &&
                autoComplModel.stream().findFirst().get().getLastConsultDate().isBefore(LocalDateTime.now()) &&
                ChronoUnit.DAYS.between(autoComplModel.stream().findFirst().get().getLastConsultDate(), LocalDateTime.now()) <= 30;
    }
}
