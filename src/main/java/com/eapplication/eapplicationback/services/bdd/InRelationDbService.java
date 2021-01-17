package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.InRelationDb;
import com.eapplication.eapplicationback.repository.InRelationDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InRelationDbService {

	@Autowired
	InRelationDbRepository repository;

	public InRelationDb save (InRelationDb entity){
		return this.repository.save(entity);
	}

	public void saveAll (List<InRelationDb> inRelationDbList){
		if(inRelationDbList != null) {
			this.repository.saveAll(inRelationDbList);
		}
	}

}
