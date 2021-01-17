package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.RelationTypeDb;
import com.eapplication.eapplicationback.repository.RelationTypeDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationTypeDbService {

	@Autowired
	RelationTypeDbRepository repository;

	public RelationTypeDb save (RelationTypeDb entity){
		return this.repository.save(entity);
	}

	public void saveAll (List<RelationTypeDb> relationTypeDbList){
		if (relationTypeDbList != null) {
			this.repository.saveAll(relationTypeDbList);
		}
	}

}
