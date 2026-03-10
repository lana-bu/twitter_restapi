package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.regex.Pattern;

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
			
			String tweetId = ""; // must initialize with value or else error is thrown
			String creationTime = "";
			String content = "";
			
			String pattern = "{ \"Tweet_ID\":%s, \"Creation_Time\":%s, \"Tweet_Content\":%s}";
			String tweetData = "";
			StringBuilder tweetsJson = new StringBuilder();
			tweetsJson.append("["); // to group JSON array
			
			for (JsonNode tweet : tweets) {
				tweetId = tweet.get("id_str").toString(); 				
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
	
	@RequestMapping(value="/tweets/links", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getLinksFromTweets() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			String urlRegex = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";
			Pattern urlPatter = Pattern.compile(urlRegex);
			
			String tweetId = ""; // must initialize with value or else error is thrown
			String content = "";
			
			String[] linksArr = {"test1", "test2"};
			StringBuilder links = new StringBuilder();
			
			String pattern = "{ \"Tweet_ID\":%s, \"Links\":%s}";
			String tweetData = "";
			StringBuilder tweetsJson = new StringBuilder();
			tweetsJson.append("["); // to group JSON array
			
			for (JsonNode tweet : tweets) {
				tweetId = tweet.get("id_str").toString();	
				content = tweet.get("text").toString();
				
				for (int i = 0; i < linksArr.length; i++ ) {
					links.append(linksArr[i]);
					if (i < linksArr.length - 1) {
						links.append(", ");
					}
				}
				
				tweetData = String.format(pattern, tweetId, links.toString());
				tweetsJson.append(tweetData).append(", "); // to mark end of JSON array object
				links.setLength(0); // reset string builder
			}
			
			tweetsJson.replace(tweetsJson.length() - 2, tweetsJson.length(), ""); // delete last two characters ", " at the end
			tweetsJson.append("]"); // close JSON array group
			
			return tweetsJson.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}

	@RequestMapping(value="/tweets/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getTweetDetails() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			return tweets.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}

	@RequestMapping(value="/users/{screen_name}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getUserDetails() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			return tweets.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}
}
