package com.aram.erpcrud.movements.application.query;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.common.SafePagination;
import com.aram.erpcrud.movements.application.MovementSpecifications;
import com.aram.erpcrud.movements.application.clients.ProductServiceClient;
import com.aram.erpcrud.movements.application.clients.UsersServiceClient;
import com.aram.erpcrud.movements.domain.Movement;
import com.aram.erpcrud.movements.domain.MovementRepository;
import com.aram.erpcrud.movements.domain.MovementType;
import com.aram.erpcrud.movements.domain.ProductQuantity;
import com.aram.erpcrud.movements.payload.*;
import com.aram.erpcrud.products.payload.ProductDTO;
import com.aram.erpcrud.personaldetails.payload.PersonalNameDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
public class GetMovementsQueryHandler {

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

    private final MovementRepository movementRepository;
    private final UsersServiceClient usersServiceClient;
    private final ProductServiceClient productServiceClient;

    public GetMovementsQueryHandler(
            SafePagination safePagination,
            MovementRepository movementRepository,
            UsersServiceClient usersServiceClient,
            ProductServiceClient productServiceClient
    ) {
        this.safePagination = safePagination;
        this.movementRepository = movementRepository;
        this.usersServiceClient = usersServiceClient;
        this.productServiceClient = productServiceClient;
    }

    public PageResponse<MovementDTO> handle(GetMovementsQuery query) {
        log.error("handling query {}", query.toString());
        PageRequest pageRequest = getMovementPageRequest(query);
        Specification<Movement> movementSpecification = getMovementSpecification(query);
        Page<Movement> page = movementRepository.findAll(movementSpecification, pageRequest);
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

    private MovementDTO toMovementDto(Movement movement) {
        PersonalNameDTO responsibleName = usersServiceClient.getResponsible(movement.getResponsibleId());
        List<String> productIds = getProductsIds(movement.getProductQuantities());
        List<ProductDTO> products = productServiceClient.getProducts(productIds);
        List<ProductQuantityDTO> productQuantities = assembleProductQuantities(products, movement.getProductQuantities());

        return new MovementDTO(
                movement.getId(),
                responsibleName,
                toMovementTypeDto(movement.getMovementType()),
                productQuantities,
                movement.getObservations(),
                movement.getTimestamp()
        );
    }

    private Specification<Movement> getMovementSpecification(GetMovementsQuery query) {
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

    private List<String> getProductsIds(List<ProductQuantity> productQuantities) {
        return productQuantities.stream().map(ProductQuantity::getProductId).toList();
    }

    private List<ProductQuantityDTO> assembleProductQuantities(
            List<ProductDTO> products,
            List<ProductQuantity> productQuantities
    ) {
        return productQuantities.stream()
                .map(productQuantity -> toProductQuantityDto(productQuantity, products))
                .toList();
    }

    private ProductQuantityDTO toProductQuantityDto(ProductQuantity productQuantity, List<ProductDTO> products) {
        ProductDTO product = products.stream()
                .filter(aProduct -> aProduct.id().equals(productQuantity.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR));

        return new ProductQuantityDTO(product, productQuantity.getQuantity());
    }

    private MovementTypeDTO toMovementTypeDto(MovementType movementType) {
        return new MovementTypeDTO(movementType.getId(), movementType.getDescription());
    }

}