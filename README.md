# eapplication-back


--->  appel avec deux paramètres ( $nom + $relation ) 
-->Return [  on retourne : 
1- la liste des noeuds (Entry)
2- la liste des relations entrantes (in_relation) qui ont un poids positif
3- node_type (table "index des types de noeuds")
4- relation_type (table "index des type de relation")
]

view-source:http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=$nom&rel=$relation


---> appel avec un seul paramètre ( $nom  )  
---> Return [  <definition>
	raffinement :
		* rt;1;'r_raff_sem';... : exemple pour un reffinement sémantique --> je regarde dans les rt s'il existe un type de relation nommé
		r_raff_sem ou portant l'id n° : 1, après je check dans les relations sortantes (r;61159560;150;2667669;**1**;40) pour récupérer *
		tous les raffinements sémantiques (si le poids d'une relation sortante est négatif ou 0 ne pas l'afficher)
		
		* rt;35;'r_meaning/glose';... : pour la glose --> pareil
]

view-source:http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=$nom



---> appel avec deux paramètres ( $nom + $relation ) 
view-source:http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=chat%3E63235
-->Return [  



]


Données bruts  ==> dans un bloc 

<CODE> 
	// DUMP pour le terme 'chat' (eid=150)
	<def>
		Définition du mot saisi
	</def>
	
	// les types de noeuds (Nodes Types) : nt;ntid;'ntname'   ======> (node type; node id; node type name) ==> (3 colonnes)
		nt;1;'n_term'
		nt;2;'n_form'
		
	// les noeuds/termes (Entries) : e;eid;'name';type;w;'formated name'  ===> (entry; entry id; entry name; type (lien avec node id); w; )
	
	// les types de relations (Relation Types) : rt;rtid;'trname';'trgpname';'rthelp' 
	
	// les relations sortantes : r;rid;node1;node2;type;w 
	
	// les relations entrantes : r;rid;node1;node2;type;w 
	
	// END


</CODE>


----------------------------------------------
node_type
id | name

entry
id | name | type (foreign key : node_type.id) | weight | formated_name


relation_type
id | name | trgpname | help

out_relation
id | node_id_1 (entry.id du mot saisi : ex chat) | node_id_2 (entry.id) | relation_type (relation_type.id) | weight

in_relation
id | node_id_1 (entry.id) | node_id_2 (entry.id du mot saisi : ex chat) | relation_type (relation_type.id) | weight

