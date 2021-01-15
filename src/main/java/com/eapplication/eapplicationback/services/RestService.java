package com.eapplication.eapplicationback.services;

import com.eapplication.eapplicationback.tools.Encoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestService {

	@Value("${rezo.dump.base.url}") private String resoDumpBaseUrl;

	@Autowired private RestTemplate restTemplate;

	public RestService() {
	}

	// TODO mettre dans les services
	public String callZero(@RequestParam String gotermrel, @RequestParam(required = false) String rel)
			throws UnsupportedEncodingException {

		StringBuilder builder = new StringBuilder("http://www.jeuxdemots.org/rezo-dump.php?gotermrsubmit=Chercher&gotermrel=");
		builder.append(URLEncoder.encode("chat>mammifère",StandardCharsets.ISO_8859_1.toString()));
		builder.append("&rel=");
		URI uri = URI.create(builder.toString());

		return restTemplate.getForObject(
				uri,
				String.class);

//		Map<String, String> vars = new HashMap<>();
//		vars.put("gotermrel", gotermrel);
//		vars.put("rel", rel);
//
//		return restTemplate.getForObject(
//				"http://www.jeuxdemots.org/rezo-dump.php?gotermrsubmit=Chercher&gotermrel={gotermrel}&rel={rel}",
//				String.class, vars);


//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);
//		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
//
//		// param à envoyer:
//		String reelParam = "";
//		//encode (les espaces sont à remplacer par "+" le reste est encodé)
//		//String paramPlusSeparator = StringUtils.replace(gotermrel, " ", "+");
//
////		String[] params = StringUtils.split(gotermrel, " ");
////		boolean needToEncode = false;
////
////		for (String param : params) {
////			// on encode le param
////			String encodedParam = Encoding.toISO8859_1UrlEncode(param);
////			if (encodedParam.length() != param.length()) {
////				needToEncode = true;
////				break;
////			}
////		}
////
////		System.out.println("param avant encodage " + gotermrel);
////		String encodedParam;
////		if (needToEncode) {
////			System.out.println("nécessité d'encoder " + gotermrel);
////			encodedParam = Encoding.toISO8859_1UrlEncode(gotermrel);
////			System.out.println("param après encodage " + encodedParam);
////		} else {
////			System.out.println("pas d'encodage nécessaire " + gotermrel);
////			encodedParam = gotermrel;
////		}
////
////		UriComponentsBuilder urlBuilder;
////		if (needToEncode) {
////			urlBuilder = UriComponentsBuilder.fromHttpUrl(resoDumpBaseUrl).queryParam("gotermsubmit", "Chercher")
////					.queryParam("gotermrel", gotermrel)
////					.encode(StandardCharsets.ISO_8859_1)/*.encode(StandardCharsets.ISO_8859_1)*/
////					.queryParam("rel", rel)/*.encode(StandardCharsets.ISO_8859_1)*/;
////
////		} else {
////			urlBuilder = UriComponentsBuilder.fromHttpUrl(resoDumpBaseUrl).queryParam("gotermsubmit", "Chercher")
////					.queryParam("gotermrel", gotermrel)/*.encode(StandardCharsets.ISO_8859_1)*/
////					.queryParam("rel", rel)/*.encode(StandardCharsets.ISO_8859_1)*/;
////		}
//
//		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(resoDumpBaseUrl).queryParam("gotermsubmit", "Chercher")
//				.queryParam("gotermrel", gotermrel)
//				.queryParam("rel", rel)/*.encode(StandardCharsets.ISO_8859_1)*/;
//
//		HttpEntity<String> response = restTemplate
//				.exchange(urlBuilder.build(false).encode().toUriString(), HttpMethod.GET, httpEntity, String.class);
//
//		System.out.println("Response Headers " + response.getHeaders());
//		System.out.println("Response body : " + response.getBody().trim());
//
//		return response;

	}
}
