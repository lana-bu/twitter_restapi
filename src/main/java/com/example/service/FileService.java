package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	@Autowired
	private ResourceLoader resources;
	
	public String readFileFromResources(String filename) throws IOException {
		Resource file = resources.getResource("classpath:" + filename); // accesses file under src/main/resources
		InputStream inputStream = file.getInputStream(); // take file's contents as input
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); // make a reader for input from file
		StringBuilder content = new StringBuilder(); // to append each new line read onto
		String line;
		
		while ((line = reader.readLine()) != null) { // assign each new line read to line, then check if at end of file
			content.append(line).append("\n");
		}
		
		reader.close();
		
		return content.toString().trim();
	}
}
