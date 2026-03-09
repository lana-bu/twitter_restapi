package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.File;

public class TweetData {
	ObjectMapper mapper = new ObjectMapper();
	JsonNode root = mapper.readTree(new File("../src/main/resources/favs.json"));

	
	@RequestMapping(value="/tweets", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE) // or just print as string
	public String getAllTweets() {

		for (JsonNode tweet : root) { 
			// get tweet data, maybe as string formatted together, then store in array, then print out array
		}

	}
}
