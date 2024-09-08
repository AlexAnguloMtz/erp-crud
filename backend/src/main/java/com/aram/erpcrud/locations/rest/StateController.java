package com.aram.erpcrud.locations.rest;

import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.domain.StateRepository;
import com.aram.erpcrud.locations.payload.StateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/locations")
public class StateController {

    private final StateRepository stateRepository;

    public StateController(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @GetMapping("/states")
    public ResponseEntity<Iterable<StateDTO>> getStates() {
        return new ResponseEntity<>(
            stateRepository.findAll().stream().map(this::toStateDTO).toList(),
            HttpStatus.OK
        );
    }

    private StateDTO toStateDTO(State state) {
        return new StateDTO(state.getId(), state.getName());
    }

}