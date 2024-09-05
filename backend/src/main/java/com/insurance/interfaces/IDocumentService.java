package com.insurance.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.response.DocumentResponse;



public interface IDocumentService {
    String uploadFile(String documentType, MultipartFile file, String username) throws IOException;

	List<DocumentResponse> getDocuments(String customer_id, String username);

	ResponseEntity<Resource> downloadFile(String username, String document_id);

	
}
