package com.eapplication.eapplicationback.services;

import com.eapplication.eapplicationback.models.bdd.AutoComplModel;
import com.eapplication.eapplicationback.repository.AutoComplRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoComplService {

    private AutoComplRepository autoComplRepository;

    public AutoComplService(AutoComplRepository autoComplRepository) {
        this.autoComplRepository = autoComplRepository;
    }

//    /**
//     * @param listResult
//     * @param pageSize   taille de la page
//     * @param pageNumber numéro de la page
//     *
//     * @return List des résultats au format String et le nombre de résultats
//     */
//    public AutoCompDTO findAutoCompl(List<String> listResult, int pageSize, int pageNumber) {
////        String queryAutoCompl = Builder.buildAutoQuery(this.checkParamsOfAutoComplete(autoComplFilters));
//
////        log.info("FileFilterService -> Requête consrtuite pour l'autocompletion : {}", queryAutoCompl);
//        int countResult = listResult.size();
//
//        List<String> results = ppafRepository.autoCompleteCustomQuery(queryAutoCompl, pageSize, pageNumber);
//
//        log.info("FileFilterService -> Résultats de la requête : {}", results);
//
//        return PpafAutoComplResultDTO.builder().listValues(results).countValues(countResult).build();
//    }

    public void saveAll(List<AutoComplModel> autoComplModels) {
        autoComplRepository.saveAll(autoComplModels);
//        try{
//            autoComplRepository.saveAll(autoComplModels);
//        } catch (DataIntegrityViolationException e){
//            System.out.println("Exception + " + e);
//            autoComplModels.parallelStream().forEach(autoComplModel -> autoComplRepository.findById(
//                    autoComplModel.getNode()).ifPresentOrElse(acM -> System.out.println(acM.getNode() + " : exist deja "),
//                    () -> autoComplRepository.save(autoComplModel)));
//        }
    }

    /**
     * delete all
     */
    public void deleteAll(){
        autoComplRepository.deleteAll();
    }

    public long count(){
        return autoComplRepository.count();
    }

    public List<AutoComplModel> findByNode(String node){
        return autoComplRepository.findByNode(node);
    }

    public Page<AutoComplModel> findByNodeStartsWith(String node, int page, int size){
        return autoComplRepository.findByNodeStartsWith(node, PageRequest.of(page, size));
    }


}
