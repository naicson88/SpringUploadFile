package com.naicson.fileupload.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.naicson.fileupload.message.ResponseFile;
import com.naicson.fileupload.message.ResponseMessage;
import com.naicson.fileupload.model.FileDB;
import com.naicson.fileupload.service.FileStorageService;

@RestController
@CrossOrigin("*")
public class FileController {
	
	@Autowired
	private FileStorageService fileService;
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file){
		String message = "";
		try {
			fileService.saveFile(file);
			message = "Uploaded the file successfully "+file.getOriginalFilename()+"!";
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(message), HttpStatus.OK);
			
		}catch(Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename()+"!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@GetMapping("/all-files")
	public ResponseEntity<List<ResponseFile>> getListFiles(){
		
		List<ResponseFile> files = fileService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/files/")
					.path(dbFile.getId())
					.toUriString();
			
			return new ResponseFile(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}
	
	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id){
		FileDB fileDB = fileService.getFile(id);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}
}
