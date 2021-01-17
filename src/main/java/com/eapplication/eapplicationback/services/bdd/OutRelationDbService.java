package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.OutRelationDb;
import com.eapplication.eapplicationback.repository.OutRelationDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutRelationDbService {

	@Autowired
	OutRelationDbRepository repository;

	public OutRelationDb save (OutRelationDb entity){
		return this.repository.save(entity);
	}

	public void saveAll (List<OutRelationDb> outRelationDbList){
		if (outRelationDbList != null) {
			this.repository.saveAll(outRelationDbList);
		}
	}

}
