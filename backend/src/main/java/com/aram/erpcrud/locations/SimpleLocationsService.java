package com.aram.erpcrud.locations;

import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.domain.StateRepository;
import com.aram.erpcrud.locations.payload.StateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Component
public class SimpleLocationsService implements LocationsService {

    private final StateRepository stateRepository;

    public SimpleLocationsService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public StateDTO findStateById(String id) {
        return stateRepository.findById(id)
                .map(this::toStateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<StateDTO> findStates(Set<String> stateIds) {
        return stateRepository.findAllById(stateIds).stream()
                .map(this::toStateDto)
                .toList();
    }

    private StateDTO toStateDto(State state) {
        return new StateDTO(state.getId(), state.getName());
    }
}
