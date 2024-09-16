package com.aram.erpcrud.locations;

import com.aram.erpcrud.locations.payload.StateDTO;

import java.util.List;
import java.util.Set;

public interface LocationsService {

    StateDTO findStateById(String id);

    List<StateDTO> findStates(Set<String> stateIds);

}