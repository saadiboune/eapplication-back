package com.eapplication.eapplicationback.tools;

import com.eapplication.eapplicationback.constantes.FileConstantes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FileJsonUtils {

	public static List<JSONObject > readJsonFile() {
		
		List<JSONObject> result = new ArrayList<>();

		// converting json to Map
		byte[] mapData;
		try {
			mapData = Files.readAllBytes(Paths.get(FileConstantes.SYMBOLE_A_JSON));
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(mapData, new TypeReference<List<JSONObject>>(){});
			
			//log.info("FileJsonUtils ->résultat de lecture du fichier {}", result);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		return result;
	}
}
