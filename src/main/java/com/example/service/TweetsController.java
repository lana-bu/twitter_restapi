package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

@RestController
public class TweetsController {
	
	@Autowired
	private FileService fileReader;	
	
	@RequestMapping(value="/tweets", method=RequestMethod.GET) // or just print as string
	public String getAllTweets() {
		try {
			return fileReader.readFileFromResources("favs.json");
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
		
		

//		for (JsonNode tweet : root) { 
//			// get tweet data, maybe as string formatted together, then store in array, then print out array
//			String creation_time = root.get("created_at");
//			String id = ;
//			String text = ;
//		}

	}
}
