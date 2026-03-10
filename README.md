# twitter_restapi
This project is for my Assignment #2 in CIS 376 (Software Engineering II) at UM-Dearborn. It is a RESTful API that uses an archive of tweets (provided by my professor in a JSON file) to collect different data fields from and output to the user in a web browser. I created it to become more familiar with creating REST APIs using the SpringBoot framework and testing them with Postman.
## Run Instructions
### Steps
1. Clone the repository
   - Download the source code to your local machine.
   - Open the repository in Eclipse IDE or another IDE of your choice.
     - Make sure you have JDK version 8 or later installed on your machine.
     - Make sure your IDE is set up with the necessary extensions to run a Java program using the Spring Boot framework.
2. Run the program
   - Go to the folder path twitter_restapi/src/main/java/com.example.service.
   - In com.example.service, find TwitterRestapiApp.java and run it as a Spring Boot App.
3. Test `getAllTweets()`
   - Go to http://localhost:8080/tweets on your browser or in Postman (as a GET request).
     - Port 8080 is the default port for Spring Boot apps, but check the console after running to see if it's using a different port (if so, replace 8080 in the URL with that port number).
       <img width="922" height="191" alt="image" src="https://github.com/user-attachments/assets/04314459-f18e-4dfe-a80e-ff055258420f" />
   - Make sure the output is accurate (JSON String includes the ID, creation time, and content of each tweet).
     <img width="797" height="407" alt="image" src="https://github.com/user-attachments/assets/7f482867-aa02-4beb-9d38-4953bee3202e" />
4. Test `getLinksFromTweets()`
   - Go to http://localhost:8080/tweets/links on your browser or in Postman (as a GET request).
   - Make sure the output is accurate (JSON String includes the ID and the links found in the content of each tweet).
     <img width="263" height="449" alt="image" src="https://github.com/user-attachments/assets/ac1103ff-f69e-43af-b87b-a56ee5e71716" />
5. Test `getTweetDetails()`
   - Go to the folder path twitter_restapi/src/main/resources.
   - In resources, find favs.json and open the file.
   - Copy the ID of one of the tweets.
   - Type http://localhost:8080/tweets/ and paste the ID onto the end of the URL, then go to it on your browser or in Postman (as a GET request).
   - Make sure the output is accurate (JSON String includes the creation time and content of the tweet as well as the screen name of the user who posted it).
     - Example: http://localhost:8080/tweets/311975360667459585
       <img width="788" height="85" alt="image" src="https://github.com/user-attachments/assets/542dae2e-a3ea-4b9d-b1e7-3ed8b99944b3" />
7. Test `getUserDetails()`
   - Go to the folder path twitter_restapi/src/main/resources.
   - In resources, find favs.json and open the file.
   - Copy the screen name of one of the users who posted a tweet.
   - Type http://localhost:8080/users/ and paste the screen name onto the end of the URL, then go to it on your browser or in Postman (as a GET request).
   - Make sure the output is accurate (JSON String includes the location, description, followers count, and friends count of the user profile).
     - Example: http://localhost:8080/users/timoreilly
       <img width="729" height="84" alt="image" src="https://github.com/user-attachments/assets/c054924f-8e24-4e13-b9d5-3c142f5b885d" />
### Using A Different Tweet Archive
If you want to test this RESTful API with a different tweet archive, make sure to format it as a JSON file with all of the same key-value pairs as favs.json (or at least created_at, id, id_str, text, and user; user's value should be a JSON object that at least has the keys screen_name, location, description, followers_count, and friends_count). Save this file in the resources folder (twitter_restapi/src/main/resources). Update the source code of TweetsController.java (in twitter_restapi/src/main/java/com.example.service) so that the file read into `tweetsArchive` by `fileReader` at the beginning of each GET request function is your tweets archive instead of favs.json.
- Example: `String tweetsArchive = fileReader.readFileFromResources("yourArchive.json");`
## Sources
### Code Help
- Reading a file and converting to string: https://www.geeksforgeeks.org/advance-java/read-file-from-resources-folder-in-spring-boot/
- Converting JSON String to JsonNode and getting specific data from it: https://www.baeldung.com/jackson-json-to-jsonnode
- Converting JsonNode to String: https://www.baeldung.com/java-jsonnode-astext-vs-tostring
- Iterating over objects in JSON: https://www.baeldung.com/jackson-json-node-tree-model
- Removing ending characters from a String using replace(): https://www.baeldung.com/java-remove-last-character-stringbuilder
- Finding links in a String with regex: https://uibakery.io/regex-library/url-regex-java
- Appending double quotes to Strings in array: https://stackoverflow.com/questions/18227938/java-append-quotes-to-strings-in-an-array-and-join-strings-in-an-array
- Using path variables: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
- Converting String to Long: https://stackoverflow.com/questions/7693324/how-to-convert-string-to-long-in-java
- Adding elements to dynamic ArrayList: https://www.geeksforgeeks.org/java/how-to-add-an-element-to-an-array-in-java/
- Check if an ArrayList contains a value: https://stackoverflow.com/questions/4404084/check-if-a-value-exists-in-arraylist
