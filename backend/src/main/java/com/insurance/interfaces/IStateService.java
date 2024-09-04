package com.insurance.interfaces;

import com.insurance.request.StateRequest;
import com.insurance.response.StateResponse;
import com.insurance.util.PagedResponse;

public interface IStateService {

	String createState(StateRequest stateRequest);

	String updateState(String id, StateRequest stateRequest);

	StateResponse getStateById(String id);

	PagedResponse<StateResponse> getAllStates(int page, int size, String sortBy, String direction);

	String deactivateStateById(String id);

	String activateStateById(String id);

}
