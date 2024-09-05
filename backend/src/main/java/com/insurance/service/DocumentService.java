package com.insurance.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.entities.Customer;
import com.insurance.entities.Document;
import com.insurance.entities.Employee;
import com.insurance.entities.User;
import com.insurance.enums.DocumentType;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IDocumentService;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.DocumentRepository;
import com.insurance.repository.EmployeeRepository;
import com.insurance.repository.UserRepository;
import com.insurance.response.DocumentResponse;
import com.insurance.util.UniqueIdGenerator;


@Service
public class DocumentService implements IDocumentService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UniqueIdGenerator uniqueIdGenerator;

    @Override
    public String uploadFile(String documentType, MultipartFile file, String username) throws IOException {
        User user = userRepository.findByUsernameOrEmail(username, username)
                                  .orElseThrow(() -> new ResourceNotFoundException("User is not available"));
        Customer customer = customerRepository.findByUser(user);
        

        String projectRoot = System.getProperty("user.dir");
        String uploadPath = projectRoot + File.separator + UPLOAD_DIR + customer.getCustomerId();
        File customerDir = new File(uploadPath);
        if (!customerDir.exists()) {
            customerDir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String newFileName = documentType + fileExtension;

        DocumentType documentTypeEnum;
        try {
            documentTypeEnum = DocumentType.valueOf(documentType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        Document existingDocument = documentRepository.findByCustomerAndDocumentName(customer, documentTypeEnum);
        if (existingDocument != null) {
            File existingFile = new File(existingDocument.getDocumentPath());
            if (existingFile.exists() && !existingFile.delete()) {
                throw new IOException("Failed to delete the existing file");
            }
            documentRepository.delete(existingDocument);
        }

        File dest = new File(customerDir, newFileName);
        file.transferTo(dest);

        Document document = new Document();
        document.setDocumentId(uniqueIdGenerator.generateUniqueId(Document.class));
        document.setDocumentName(documentTypeEnum);
        document.setDocumentType(fileExtension);
        document.setUploadDate(LocalDate.now());
        document.setDocumentPath(dest.getAbsolutePath());
        document.setCustomer(customer);
        documentRepository.save(document);

        return newFileName;
    }

	@Override
	public List<DocumentResponse> getDocuments(String customer_id, String username) {
		User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User is not available"));
		
		Employee employee = employeeRepository.findByUser(user).orElse(null);
		if(employee == null) {
			throw new ApiException("Unauthorized");
		}
		
		Optional<Customer> oCustomer = customerRepository.findById(customer_id);
		if(oCustomer.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found");
		}
		List<Document> documents = documentRepository.findByCustomer(oCustomer.get());
		List<DocumentResponse> documentResponses = new ArrayList<>();
		for(Document document: documents) {
			DocumentResponse documentResponse = new DocumentResponse();
			documentResponse.setDocumentId(document.getDocumentId());
			documentResponse.setDocumentName(document.getDocumentName());
			documentResponse.setDocumentType(document.getDocumentType());
			documentResponse.setUploadDate(document.getUploadDate());
			documentResponses.add(documentResponse);
		}
		return documentResponses;
	}

	@Override
	public ResponseEntity<Resource> downloadFile(String username, String document_id) {
	    User user = userRepository.findByUsernameOrEmail(username, username)
	            .orElseThrow(() -> new ResourceNotFoundException("User is not available"));

	    Employee employee = employeeRepository.findByUser(user).orElse(null);
	    if (employee == null) {
	        throw new ApiException("Unauthorized");
	    }

	    Document document = documentRepository.findById(document_id)
	            .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

	    File file = new File(document.getDocumentPath());
	    if (!file.exists()) {
	        throw new ResourceNotFoundException("File not found on the server");
	    }

	    Resource resource = new FileSystemResource(file);

	    String contentType = "application/octet-stream";
	    try {
	        contentType = Files.probeContentType(file.toPath());
	    } catch (IOException e) {
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
	            .body(resource);
	}

}
