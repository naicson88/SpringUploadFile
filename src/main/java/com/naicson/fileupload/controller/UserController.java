package com.naicson.fileupload.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.naicson.fileupload.model.User;
import com.naicson.fileupload.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(value = "/user/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<User> upload(@RequestPart("user") String user, @RequestPart("file") List<MultipartFile> file) {
		User userJson = new User();
		try {
			userJson = userService.getJson(user, file);
			
	      long bytes =  Files.copy(file.get(0).getInputStream(), Paths.get("C:\\Cards\\"+file.get(0).getOriginalFilename()));
	      System.out.println("Arquivo Salvo na pasta, tamanho: " + bytes);
	      
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<User>(userJson, HttpStatus.OK);
	}
}
