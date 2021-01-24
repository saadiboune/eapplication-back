package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.EntryDb;
import com.eapplication.eapplicationback.models.ihm.HomeModel;
import com.eapplication.eapplicationback.repository.EntryDbRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntryDbService {

	@Autowired
	EntryDbRepository repository;

	public EntryDb save (EntryDb entity){
		return this.repository.save(entity);
	}

	public void saveAll(List<EntryDb> entryDbList) {
			this.repository.saveAll(entryDbList.stream()
					.filter(Objects::nonNull)
					.collect(Collectors.toList()));
	}

	public Page<EntryDb> findEntriesRelatedToSpecificRelationType(int relationTypeId, int nodeIdSearched, int page, int size){
		return repository.findEntriesWithSpecificRelationType(relationTypeId, nodeIdSearched, PageRequest.of(page, size));
	}

	@Transactional
	public void update(List<EntryDb> entryDbs){
		log.info("sauvegarde EntryDb {}", entryDbs.size());
		List<EntryDb> entryDbsNotNull = entryDbs.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		// delete
		this.repository.deleteByIdIn(entryDbsNotNull.stream().map(EntryDb::getId).collect(Collectors.toList()));
		this.repository.saveAll(entryDbsNotNull);
	}

	public void deleteAll(List<EntryDb> entryDbs){
		this.repository.deleteAll(entryDbs);
	}

//	public Page<HomeModel> homePageFromBDD(String wordToSearch){
//		return repository.findEntriesWithSpecificRelationType(relationTypeId, nodeIdSearched, PageRequest.of(page, size));
//	}

	public int getIdByName(String searchedWord) {
		return this.repository.findIdByName(searchedWord).stream().findFirst().isPresent() ?
				this.repository.findIdByName(searchedWord).stream().findFirst().get() : 0;
	}
}
