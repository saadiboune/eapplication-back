package com.eapplication.eapplicationback.services.bdd;

import com.eapplication.eapplicationback.models.bdd.NodeTypeDb;
import com.eapplication.eapplicationback.repository.NodeTypeDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeTypeDbService {

	@Autowired
	NodeTypeDbRepository repository;

	public NodeTypeDb save (NodeTypeDb entity){
		return this.repository.save(entity);
	}

	public void saveAll (List<NodeTypeDb> nodeTypeDbList){
		if (nodeTypeDbList != null) {
			this.repository.saveAll(nodeTypeDbList);
		}
	}

}
