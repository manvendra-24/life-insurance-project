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

import com.insurance.entities.City;
import com.insurance.entities.State;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.ICityService;
import com.insurance.repository.CityRepository;
import com.insurance.repository.StateRepository;
import com.insurance.request.CityRequest;
import com.insurance.response.CityResponse;
import com.insurance.util.Mappers;
import com.insurance.util.PagedResponse;
import com.insurance.util.UniqueIdGenerator;

@Service
public class CityService implements ICityService {

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    @Autowired
    CityRepository cityRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    Mappers mappers;

    @Autowired
    UniqueIdGenerator uniqueIdGenerator;

    @Override
    public String createCity(CityRequest cityRequest) {
        logger.info("Creating a new city with name: {}", cityRequest.getName());
        State state = stateRepository.findById(cityRequest.getState_id()).orElse(null);
        if (state != null) {
            if (state.isActive()) {
                City city = mappers.cityRequestToCity(cityRequest);
                city.setCityId(uniqueIdGenerator.generateUniqueId(City.class));
                cityRepository.save(city);
                logger.info("City added successfully with ID: {}", city.getCityId());
                return "City Added Successfully";
            } else {
                logger.warn("State is inactive for state ID: {}", cityRequest.getState_id());
                throw new ApiException("State is Inactive");
            }
        } else {
            logger.warn("State not found for state ID: {}", cityRequest.getState_id());
            throw new ResourceNotFoundException("State not found");
        }
    }

    @Override
    public String deactivateCity(String id) {
        logger.info("Deactivating city with ID: {}", id);
        City city = cityRepository.findById(id).orElse(null);
        if (city != null) {
            if (city.isActive()) {
                city.setActive(false);
                cityRepository.save(city);
                logger.info("City deactivated successfully with ID: {}", id);
            } else {
                logger.warn("City is already inactive with ID: {}", id);
                throw new ApiException("City is already inactive");
            }
        } else {
            logger.warn("City not found with ID: {}", id);
            throw new ResourceNotFoundException("City not found");
        }
        return "Deactivated Successfully";
    }

    @Override
    public CityResponse getCityById(String id) {
        logger.info("Fetching city with ID: {}", id);
        City city = cityRepository.findById(id).orElse(null);
        if (city != null) {
            CityResponse cityResponse = mappers.cityToCityResponse(city);
            logger.info("City found with ID: {}", id);
            return cityResponse;
        }
        logger.warn("City not found with ID: {}", id);
        return null;
    }

    @Override
    public String activateCity(String id) {
        logger.info("Activating city with ID: {}", id);
        City city = cityRepository.findById(id).orElse(null);
        if (city != null) {
            if (!city.isActive()) {
                city.setActive(true);
                cityRepository.save(city);
                logger.info("City activated successfully with ID: {}", id);
            } else {
                logger.warn("City is already active with ID: {}", id);
                throw new ApiException("City is already active");
            }
        } else {
            logger.warn("City not found with ID: {}", id);
            throw new ResourceNotFoundException("City not found");
        }
        return "Activated Successfully";
    }

    @Override
    public PagedResponse<CityResponse> getAllCities(int page, int size, String sortBy, String direction) {
        logger.info("Fetching all cities with pagination - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<City> cityPage = cityRepository.findAll(pageable);
        List<CityResponse> cityResponse = cityPage.getContent().stream()
                .map(city -> mappers.cityToCityResponse(city)).collect(Collectors.toList());

        logger.info("Fetched {} cities", cityResponse.size());
        return new PagedResponse<CityResponse>(cityResponse, cityPage.getNumber(), cityPage.getSize(),
                cityPage.getTotalElements(), cityPage.getTotalPages(), cityPage.isLast());
    }

    @Override
    public String updateCity(String id, CityRequest cityRequest) {
        logger.info("Updating city with ID: {}", id);
        City city = cityRepository.findById(id).orElse(null);
        if (city != null) {
            State state = stateRepository.findById(cityRequest.getState_id()).orElse(null);
            if (state != null) {
                if (!state.isActive()) {
                    logger.warn("State is inactive for state ID: {}", cityRequest.getState_id());
                    throw new ApiException("State is inactive");
                }
                if (!city.isActive()) {
                    logger.warn("City is inactive with ID: {}", id);
                    throw new ApiException("City is inactive");
                }
                city.setName(cityRequest.getName());
                city.setState(state);
                cityRepository.save(city);
                logger.info("City updated successfully with ID: {}", id);
                return "City updated Successfully";
            } else {
                logger.warn("State not found for state ID: {}", cityRequest.getState_id());
                throw new ResourceNotFoundException("State Not Found");
            }
        } else {
            logger.warn("City not found with ID: {}", id);
            throw new ResourceNotFoundException("City Not Found");
        }
    }
}
