package com.aram.erpcrud.locations;

import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.domain.StateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SimpleLocationsService implements LocationsService {

    private final StateRepository stateRepository;

    public SimpleLocationsService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State findStateById(String id) {
        return stateRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
