package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.EntryDb;
import com.eapplication.eapplicationback.repository.EntryDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryDbService {

	@Autowired
	EntryDbRepository repository;

	public EntryDb save (EntryDb entity){
		return this.repository.save(entity);
	}

	public void saveAll (List<EntryDb> entryDbList){
		if (entryDbList != null) {
			this.repository.saveAll(entryDbList);
		}
	}
}
