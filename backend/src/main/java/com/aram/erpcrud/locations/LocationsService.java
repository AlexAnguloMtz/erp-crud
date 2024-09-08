package com.aram.erpcrud.locations;

import com.aram.erpcrud.locations.domain.State;

public interface LocationsService {
    State findStateById(String id);
}
