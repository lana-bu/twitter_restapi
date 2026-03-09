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
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value="/tweets", method=RequestMethod.GET) // or just print as string
	public String getAllTweets() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			String creation_time = ""; // must initialize with value or else error is thrown
			String tweet_id = "";
			String content = "";
			
			StringBuilder tweetData = new StringBuilder();
			
			for (JsonNode tweet : tweets) {
				tweet_id = tweet.get("id_str").asText(); // asText is deprecated, but works in this context to remove double quotes
				tweetData.append(tweet_id).append("\n");
				
				creation_time = tweet.get("created_at").asText();
				tweetData.append(creation_time).append("\n");
				
				content = tweet.get("text").asText();
				tweetData.append(content).append("\n");
			}
			
			// return tweets.toString();
			return tweetData.toString().trim();
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
