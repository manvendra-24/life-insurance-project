package com.insurance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.insurance.entities.State;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IStateService;
import com.insurance.repository.StateRepository;
import com.insurance.request.StateRequest;
import com.insurance.response.StateResponse;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;
import com.insurance.util.UniqueIdGenerator;

@Service
public class StateService implements IStateService {

    private static final Logger logger = LoggerFactory.getLogger(StateService.class);

    @Autowired
    Mappers mappers;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    UniqueIdGenerator uniqueIdGenerator;

    @Override
    public String createState(StateRequest stateRequest) {
        logger.info("Creating new state with name: {}", stateRequest.getName());

        State state = mappers.stateRequestToState(stateRequest);
        state.setStateId(uniqueIdGenerator.generateUniqueId(State.class));
        stateRepository.save(state);

        logger.info("State created successfully with ID: {}", state.getStateId());
        return "State Added Successfully";
    }

    @Override
    public StateResponse getStateById(String id) {
        logger.info("Fetching state with ID: {}", id);

        State state = stateRepository.findById(id).orElse(null);
        if (state != null) {
            StateResponse stateResponse = mappers.stateToStateResponse(state);
            logger.info("State found with ID: {}", id);
            return stateResponse;
        }

        logger.warn("State not found with ID: {}", id);
        return null;
    }

    @Override
    public PagedResponse<StateResponse> getAllStates(int page, int size, String sortBy, String direction) {
        logger.info("Fetching all states with pagination - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<State> statePage = stateRepository.findAll(pageable);
        List<StateResponse> stateResponse = statePage.getContent().stream()
                .map(state -> mappers.stateToStateResponse(state)).collect(Collectors.toList());

        logger.info("Fetched {} states", stateResponse.size());
        return new PagedResponse<>(stateResponse, statePage.getNumber(), statePage.getSize(),
                statePage.getTotalElements(), statePage.getTotalPages(), statePage.isLast());
    }

    @Override
    public String deactivateStateById(String id) {
        logger.info("Deactivating state with ID: {}", id);

        State state = stateRepository.findById(id).orElse(null);
        if (state != null) {
            if (state.isActive()) {
                state.setActive(false);
                stateRepository.save(state);

                logger.info("State deactivated successfully with ID: {}", id);
            } else {
                logger.warn("State is already inactive with ID: {}", id);
                throw new ApiException("State is already inactive");
            }
        } else {
            logger.warn("State not found with ID: {}", id);
            throw new ResourceNotFoundException("State Not Found");
        }

        return "State deactivated successfully";
    }

    @Override
    public String activateStateById(String id) {
        logger.info("Activating state with ID: {}", id);

        State state = stateRepository.findById(id).orElse(null);
        if (state != null) {
            if (!state.isActive()) {
                state.setActive(true);
                stateRepository.save(state);

                logger.info("State activated successfully with ID: {}", id);
            } else {
                logger.warn("State is already active with ID: {}", id);
                throw new ApiException("State is already active");
            }
        } else {
            logger.warn("State not found with ID: {}", id);
            throw new ResourceNotFoundException("State Not Found");
        }

        return "State activated successfully";
    }

    @Override
    public String updateState(String id, StateRequest stateRequest) {
        logger.info("Updating state with ID: {}", id);

        State state = stateRepository.findById(id).orElse(null);
        if (state != null) {
            if (!state.isActive()) {
                logger.warn("Cannot update inactive state with ID: {}", id);
                throw new ApiException("State is inactive");
            }

            state.setName(stateRequest.getName());
            stateRepository.save(state);

            logger.info("State updated successfully with ID: {}", id);
        } else {
            logger.warn("State not found with ID: {}", id);
            throw new ResourceNotFoundException("State Not Found");
        }

        return "State successfully updated";
    }
}
