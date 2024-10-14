package com.aram.erpcrud.modules.stockmovements.application.query;

import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import com.aram.erpcrud.modules.stockmovements.application.MovementSpecifications;
import com.aram.erpcrud.modules.stockmovements.application.clients.ProductServiceClient;
import com.aram.erpcrud.modules.stockmovements.application.clients.PersonalDetailsServiceClient;
import com.aram.erpcrud.modules.stockmovements.domain.StockMovement;
import com.aram.erpcrud.modules.stockmovements.domain.StockMovementRepository;
import com.aram.erpcrud.modules.stockmovements.domain.StockMovementType;
import com.aram.erpcrud.modules.stockmovements.domain.StockMovementProduct;
import com.aram.erpcrud.modules.stockmovements.payload.*;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class GetMovements {

    private final SafePagination safePagination;

    private enum MovementSort {
        TIMESTAMP_ASC("timestamp", Sort.Direction.ASC),
        TIMESTAMP_DESC("timestamp", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        MovementSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    private final StockMovementRepository movementRepository;
    private final PersonalDetailsServiceClient usersServiceClient;
    private final ProductServiceClient productServiceClient;

    public GetMovements(
            SafePagination safePagination,
            StockMovementRepository movementRepository,
            PersonalDetailsServiceClient usersServiceClient,
            ProductServiceClient productServiceClient
    ) {
        this.safePagination = safePagination;
        this.movementRepository = movementRepository;
        this.usersServiceClient = usersServiceClient;
        this.productServiceClient = productServiceClient;
    }

    public PageResponse<MovementDTO> handle(GetMovementsQuery query) {
        PageRequest pageRequest = getMovementPageRequest(query);
        Specification<StockMovement> movementSpecification = getMovementSpecification(query);
        Page<StockMovement> page = movementRepository.findAll(movementSpecification, pageRequest);
        List<MovementDTO> movementDtos = page.getContent().stream().map(this::toMovementDto).toList();

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                movementDtos
        );
    }

    private MovementDTO toMovementDto(StockMovement movement) {
        PersonalNameDTO responsibleName = usersServiceClient.getPersonalName(movement.getResponsibleId());
        List<Long> productIds = getProductsIds(movement.getStockMovementProducts());
        List<ProductDTO> products = productServiceClient.getProducts(productIds);
        List<StockMovementProductDTO> productQuantities = assembleStockMovementProductDtos(products, movement.getStockMovementProducts());

        return new MovementDTO(
                movement.getId(),
                responsibleName,
                toMovementTypeDto(movement.getMovementType()),
                productQuantities,
                movement.getObservations(),
                movement.getTimestamp()
        );
    }

    private Specification<StockMovement> getMovementSpecification(GetMovementsQuery query) {
        return MovementSpecifications.withResponsibleId(query.responsibleId())
                .and(MovementSpecifications.withMovementType(query.movementTypeId()))
                .and(MovementSpecifications.after(query.start()))
                .and(MovementSpecifications.before(query.end()))
                .and(MovementSpecifications.withProduct(query.productId()));
    }

    private PageRequest getMovementPageRequest(GetMovementsQuery query) {
        MovementSort movementSort = toMovementSort(query.sort());
        Sort sort = Sort.by(Sort.Order.by(movementSort.field).with(movementSort.direction));
        return PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );
    }

    private MovementSort toMovementSort(String sort) {
        if ("timestamp-asc".equals(sort)) {
            return MovementSort.TIMESTAMP_ASC;
        }
        return MovementSort.TIMESTAMP_DESC;
    }

    private List<Long> getProductsIds(List<StockMovementProduct> productQuantities) {
        return productQuantities.stream().map(StockMovementProduct::getProductId).toList();
    }

    private List<StockMovementProductDTO> assembleStockMovementProductDtos(
            List<ProductDTO> products,
            List<StockMovementProduct> stockMovementProducts
    ) {
        return stockMovementProducts.stream()
                .map(productQuantity -> toStockMovementProductDto(productQuantity, products))
                .toList();
    }

    private StockMovementProductDTO toStockMovementProductDto(StockMovementProduct stockMovementProduct, List<ProductDTO> products) {
        ProductDTO product = products.stream()
                .filter(aProduct -> aProduct.id().equals(stockMovementProduct.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR));

        return new StockMovementProductDTO(product, stockMovementProduct.getQuantity());
    }

    private MovementTypeDTO toMovementTypeDto(StockMovementType movementType) {
        return new MovementTypeDTO(movementType.getId(), movementType.getDescription());
    }

}