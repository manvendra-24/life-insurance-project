package com.insurance.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.insurance.entities.Transaction;
import com.insurance.interfaces.ITransactionService;
import com.insurance.repository.TransactionRepository;
import com.insurance.response.TransactionResponse;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;

@Service
public class TransactionService implements ITransactionService{
	
	@Autowired
	Mappers mappers;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public PagedResponse<TransactionResponse> getAllTransactions(int page, int size, String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
	                ? Sort.by(sortBy).descending() 
	                : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    Page<Transaction> page1 = transactionRepository.findAll(pageable);
	    List<Transaction> transactions = page1.getContent();
	    List<TransactionResponse> transactionResponses = new ArrayList<>();
	    for (Transaction transaction : transactions) {
	        TransactionResponse transactionResponse = mappers.transactionToTransactionResponse(transaction);
	        transactionResponses.add(transactionResponse);
	    }
	    
	    return new PagedResponse<>(transactionResponses, page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}
	
	
}
