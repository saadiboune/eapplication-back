package com.eapplication.eapplicationback.services;

import com.eapplication.eapplicationback.constantes.Comments;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class RestService {

	@Autowired
	private RestTemplate restTemplate;

	public RestService() {
	}


	/**
	 *
	 * Appelle l'API et retourne ce qui est entre code
	 *
	 * @param termToSearch mot recherché
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String callZero(String termToSearch) throws UnsupportedEncodingException {

		String url = "http://www.jeuxdemots.org/rezo-dump.php?gotermrsubmit=Chercher&gotermrel=" + URLEncoder
				.encode(termToSearch, StandardCharsets.ISO_8859_1.toString()) + "&relation=";

		String responseBeta;
		if ((responseBeta = StringUtils
				.substringBetween(restTemplate.getForObject(URI.create(url), String.class), Comments.CODE_START,
						Comments.CODE_END)) != null) {
			return responseBeta.trim();
		}
		return responseBeta;
	}
}
