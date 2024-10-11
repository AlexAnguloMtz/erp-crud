package com.aram.erpcrud.modules.stockmovements.rest;

import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.modules.stockmovements.application.MovementFacade;
import com.aram.erpcrud.modules.stockmovements.payload.GetMovementsQuery;
import com.aram.erpcrud.modules.stockmovements.payload.MovementDTO;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/stock-movements")
public class MovementController {

    private final MovementFacade movementFacade;

    public MovementController(MovementFacade movementFacade) {
        this.movementFacade = movementFacade;
    }

    @GetMapping
    public ResponseEntity<PageResponse<MovementDTO>> getMovements(
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) Integer pageSize,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Instant start,
            @RequestParam(required = false) Instant end,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long responsibleId,
            @RequestParam(required = false) Long movementTypeId
    ) {
        GetMovementsQuery query = new GetMovementsQuery(
                pageNumber,
                pageSize,
                sort,
                start,
                end,
                productId,
                responsibleId,
                movementTypeId
        );

        return new ResponseEntity<>(movementFacade.getMovements(query), HttpStatus.OK);
    }

}