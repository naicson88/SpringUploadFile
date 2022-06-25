package com.naicson.fileupload.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naicson.fileupload.model.FileDB;
import com.naicson.fileupload.repository.FileDBRepository;

import org.springframework.util.StringUtils;

@Service
public class FileStorageService {
	
	@Autowired
	FileDBRepository fileRepository;
	
	public FileDB saveFile(MultipartFile file) throws IOException {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
		
		return fileRepository.save(fileDB);
	}
	
	public FileDB getFile(String id) {
		return fileRepository.findById(id).get();
	}
	
	public Stream<FileDB> getAllFiles(){
		return fileRepository.findAll().stream();
	}
}
