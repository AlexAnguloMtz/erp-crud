package com.aram.erpcrud.movements.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.movements.application.query.GetMovementsQueryHandler;
import com.aram.erpcrud.movements.payload.GetMovementsQuery;
import com.aram.erpcrud.movements.payload.MovementDTO;
import org.springframework.stereotype.Component;

@Component
public class MovementFacade {

    private final GetMovementsQueryHandler getMovementsQueryHandler;

    public MovementFacade(GetMovementsQueryHandler getMovementsQueryHandler) {
        this.getMovementsQueryHandler = getMovementsQueryHandler;
    }

    public PageResponse<MovementDTO> getMovements(GetMovementsQuery query) {
        return getMovementsQueryHandler.handle(query);
    }
}