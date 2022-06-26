package com.naicson.fileupload.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naicson.fileupload.model.User;

@Service
public class UserService {
	
	public User getJson(String user, List<MultipartFile> file) throws Exception {
		User  userJson = new User();
		
		try {
			
			ObjectMapper objMapper = new ObjectMapper();
			userJson = objMapper.readValue(user, User.class);
			
		} catch (Exception e) {
			System.out.println("Error import file: " + e.toString());
			throw e;
		}
		
		int fileCount = file.size();
		userJson.setCount(fileCount);
		
		return userJson;
	}
}
