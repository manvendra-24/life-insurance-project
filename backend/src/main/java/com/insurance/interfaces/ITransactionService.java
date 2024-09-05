package com.insurance.interfaces;

import com.insurance.response.TransactionResponse;
import com.insurance.util.PagedResponse;

public interface ITransactionService {

	PagedResponse<TransactionResponse> getAllTransactions(int page, int size, String sortBy, String direction);

}
