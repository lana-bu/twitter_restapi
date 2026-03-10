package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
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
			
			String urlRegex = "https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)";
			Pattern urlPattern = Pattern.compile(urlRegex);
			
			String tweetId = ""; // must initialize with value or else error is thrown
			String content = "";
			
			String pattern = "{ \"Tweet_ID\":%s, \"Links\":%s}";
			String tweetData = "";
			StringBuilder tweetsJson = new StringBuilder();
			
			tweetsJson.append("["); // to group JSON array
			
			for (JsonNode tweet : tweets) {
				tweetId = tweet.get("id_str").toString();
				content = tweet.get("text").toString();
				
				String [] linksArr = urlPattern.matcher(content).results().map(MatchResult::group).toArray(String[]::new);
				
				for (int i = 0; i < linksArr.length; i++) {
			        linksArr[i] = "\"" + linksArr[i] + "\""; // add quotes around each link for JSON formatting
			    }
								
				tweetData = String.format(pattern, tweetId, Arrays.toString(linksArr));
				tweetsJson.append(tweetData).append(", "); // to mark end of JSON array object
			}
			
			tweetsJson.replace(tweetsJson.length() - 2, tweetsJson.length(), ""); // delete last two characters ", " at the end
			tweetsJson.append("]"); // close JSON array group
			
			return tweetsJson.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}

	@RequestMapping(value="/tweets/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getTweetDetails(@PathVariable Long id) { // pass in ID provided as by user in path
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			List<Long> tweetIds = new ArrayList<Long>(); // to hold existing tweet IDs from sample data
			String tweetIdString = "";
			Long tweetId = 0L;
			
			for (JsonNode tweet : tweets) { // collect tweet IDs
				tweetIdString = tweet.get("id").toString(); // grab tweet ID as String
				tweetId = Long.parseLong(tweetIdString); // transform String into Long
				
				tweetIds.add(tweetId); // dynamically add Long tweetID to ArrayList
			}
			
			if (tweetIds.contains(id)) { // check if ID in path matches ID of an existing tweet
				JsonNode matchingTweet = mapper.createObjectNode(); // initialize JsonNode for the matching tweet
				String pattern = "{ \"Creation_Time\":%s, \"Tweet_Content\":%s, \"User_Screen_Name\":%s}";
				String tweetData = "";
				
				for (JsonNode tweet : tweets) { // find matching tweet
					if (tweet.get("id").toString().equals(Long.toString(id))) {
						matchingTweet = tweet;
						break;
					}					
				}
				
				String creationTime = matchingTweet.get("created_at").toString();				
				String content = matchingTweet.get("text").toString();
				
				JsonNode user = matchingTweet.get("user"); // grab JSON object that is value of key user
						
				String screenName = user.get("screen_name").toString();
				
				tweetData = String.format(pattern, creationTime, content, screenName);
								
				return tweetData.toString();
			} else { // throw error
				return "Error: Tweet with ID of " + Long.toString(id) + " does not exist.";
			}			
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}

	@RequestMapping(value="/users/{screen_name}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getUserDetails(@PathVariable String screen_name) {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			return tweets.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}
}
