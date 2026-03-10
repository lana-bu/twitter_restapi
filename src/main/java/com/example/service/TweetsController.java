package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;

@RestController
public class TweetsController {
	
	@Autowired
	private FileService fileReader;	// to read favs.json
	
	private ObjectMapper mapper = new ObjectMapper(); // creating once as class attribute because ObjectMapper is expensive/heavy
	
	@RequestMapping(value="/tweets", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getAllTweets() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			String creationTime = ""; // must initialize with value or else error is thrown
			String tweetId = "";
			String content = "";
			
			String pattern = "{ \"Tweet_ID\":%s, \"Creation_Time\":%s, \"Tweet_Content\":%s}";
			String tweetData = "";
			StringBuilder tweetsJson = new StringBuilder();
			tweetsJson.append("["); // to group JSON array
			
			for (JsonNode tweet : tweets) {
				tweetId = tweet.get("id_str").toString(); // asText is deprecated, but works in this context to remove double quotes				
				creationTime = tweet.get("created_at").toString();				
				content = tweet.get("text").toString();
				
				tweetData = String.format(pattern, tweetId, creationTime, content);
				tweetsJson.append(tweetData).append(", "); // to mark end of JSON array object
			}
			
			tweetsJson.replace(tweetsJson.length() - 2, tweetsJson.length(), ""); // delete last two characters ", " at the end
			tweetsJson.append("]"); // close JSON array group
			
			return tweetsJson.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}
}
