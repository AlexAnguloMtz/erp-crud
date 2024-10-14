package com.aram.erpcrud.modules.stockmovements.application;

import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.modules.stockmovements.application.query.GetMovements;
import com.aram.erpcrud.modules.stockmovements.payload.GetMovementsQuery;
import com.aram.erpcrud.modules.stockmovements.payload.MovementDTO;
import org.springframework.stereotype.Component;

@Component
public class MovementFacade {

    private final GetMovements getMovementsQueryHandler;

    public MovementFacade(GetMovements getMovementsQueryHandler) {
        this.getMovementsQueryHandler = getMovementsQueryHandler;
    }

    public PageResponse<MovementDTO> getMovements(GetMovementsQuery query) {
        return getMovementsQueryHandler.handle(query);
    }
}