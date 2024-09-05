package com.insurance.interfaces;

public interface IWithdrawalService {

	void withdrawalRequest(String token, String policy_id);

	String approveWithdrawal(String token, long withdrawal_id);

	String rejectWithdrawal(String token, long withdrawal_id);

}
