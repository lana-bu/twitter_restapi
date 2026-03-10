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
	
	// get id, creation time, and content of each tweet
	@RequestMapping(value="/tweets", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getAllTweets() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
						
			String pattern = "{ \"Tweet_ID\":%s, \"Creation_Time\":%s, \"Tweet_Content\":%s}";
			
			StringBuilder tweetsJson = new StringBuilder();
			
			tweetsJson.append("["); // to group JSON array
			
			for (JsonNode tweet : tweets) { // create JSON string for each tweet and append to larger JSON string
				String tweetId = tweet.get("id_str").toString(); 				
				String creationTime = tweet.get("created_at").toString();				
				String content = tweet.get("text").toString();
				
				String tweetData = String.format(pattern, tweetId, creationTime, content);
				tweetsJson.append(tweetData).append(", "); // append tweet JSON string and comma to mark end of JSON array object
			}
			
			tweetsJson.replace(tweetsJson.length() - 2, tweetsJson.length(), ""); // delete last two characters ", " at the end
			tweetsJson.append("]"); // close JSON array group
			
			return tweetsJson.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}
	
	// get id and all of the links found in the content for each tweet
	@RequestMapping(value="/tweets/links", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getLinksFromTweets() {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			String urlRegex = "https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)";
			Pattern urlPattern = Pattern.compile(urlRegex);
						
			String pattern = "{ \"Tweet_ID\":%s, \"Links\":%s}";
			
			StringBuilder tweetsJson = new StringBuilder();
			
			tweetsJson.append("["); // to group JSON array
			
			for (JsonNode tweet : tweets) { // create JSON string for each tweet and append to larger JSON string
				String tweetId = tweet.get("id_str").toString();
				String content = tweet.get("text").toString();
				
				String [] linksArr = urlPattern.matcher(content).results().map(MatchResult::group).toArray(String[]::new); // array of links found in content
				
				for (int i = 0; i < linksArr.length; i++) {
			        linksArr[i] = "\"" + linksArr[i] + "\""; // add quotes around each link for JSON formatting
			    }
								
				String tweetData = String.format(pattern, tweetId, Arrays.toString(linksArr));
				tweetsJson.append(tweetData).append(", "); // append tweet JSON string and comma to mark end of JSON array object
			}
			
			tweetsJson.replace(tweetsJson.length() - 2, tweetsJson.length(), ""); // delete last two characters ", " at the end
			tweetsJson.append("]"); // close JSON array group
			
			return tweetsJson.toString();
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}

	// get creation time, content, and user's screen name for a specific tweet
	@RequestMapping(value="/tweets/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getTweetDetails(@PathVariable Long id) { // pass in ID provided as by user in path
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			JsonNode matchingTweet = mapper.createObjectNode(); // initialize JsonNode for the matching tweet
			boolean matchFound = false;
			
			for (JsonNode tweet : tweets) { // find matching tweet
				if (tweet.get("id").toString().equals(Long.toString(id))) {
					matchingTweet = tweet;
					matchFound = true;
					break;
				}					
			}
			
			if (matchFound) { // create JSON string of relevant tweet data
				String pattern = "{ \"Creation_Time\":%s, \"Tweet_Content\":%s, \"User_Screen_Name\":%s}";
				
				String creationTime = matchingTweet.get("created_at").toString();				
				String content = matchingTweet.get("text").toString();
				
				JsonNode user = matchingTweet.get("user"); // grab JSON object that is value of key user
						
				String screenName = user.get("screen_name").toString();
				
				String tweetData = String.format(pattern, creationTime, content, screenName);
				
				return tweetData;
			} else { // throw error
				return "Error: Tweet with ID of " + Long.toString(id) + " does not exist.";
			}		
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}

	// get location, description, followers count, and friends count for a specific user
	@RequestMapping(value="/users/{screen_name}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getUserDetails(@PathVariable String screen_name) {
		try {
			String tweetsArchive = fileReader.readFileFromResources("favs.json");
			JsonNode tweets = mapper.readTree(tweetsArchive);
			
			List<JsonNode> users = new ArrayList<JsonNode>(); // initialize JsonNode list for users

			for (JsonNode tweet : tweets) { // collect users in ArrayList
				JsonNode user = tweet.get("user");
				
				users.add(user);
			}
			
			JsonNode matchingUser = mapper.createObjectNode(); // initialize JsonNode for the matching user
			boolean matchFound = false;
			
			for (int i = 0; i < users.size(); i++) { // find matching user
				if (users.get(i).get("screen_name").toString().equals("\"" + screen_name + "\"")) { // need to add quotes to match toString result
					matchingUser = users.get(i);
					matchFound = true;
					break;
				}					
			}
			
			if (matchFound) { // create JSON string of relevant user data
				String pattern = "{ \"Location\":%s, \"Description\":%s, \"Followers_Count\":%s, \"Friends_Count\":%s}";
				
				String location = matchingUser.get("location").toString();				
				String description = matchingUser.get("description").toString();
				String followersCount = matchingUser.get("followers_count").toString();
				String friendsCount = matchingUser.get("friends_count").toString();
								
				String userData = String.format(pattern, location, description, followersCount, friendsCount);
				
				return userData;
			} else { // throw error
				return "Error: User with screen name of " + screen_name + " does not exist.";
			}	
		} catch (IOException e) {
			return "Error occured while reading the file.";
		}
	}
}
