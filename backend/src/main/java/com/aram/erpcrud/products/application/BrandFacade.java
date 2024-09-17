package com.aram.erpcrud.products.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.products.application.query.GetBrandsQueryHandler;
import com.aram.erpcrud.products.domain.Brand;
import com.aram.erpcrud.products.domain.BrandRepository;
import com.aram.erpcrud.products.payload.BrandCommand;
import com.aram.erpcrud.products.payload.BrandDTO;
import com.aram.erpcrud.products.payload.GetBrandsQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
public class BrandFacade {

    private final GetBrandsQueryHandler getBrands;
    private final BrandRepository brandRepository;

    public BrandFacade(GetBrandsQueryHandler getBrands, BrandRepository brandRepository) {
        this.getBrands = getBrands;
        this.brandRepository = brandRepository;
    }

    public PageResponse<BrandDTO> getBrands(GetBrandsQuery query) {
        return getBrands.handle(query);
    }

    @Transactional
    public void createBrand(BrandCommand command) {
        Optional<Brand> brandOptional = brandRepository.findByName(command.name());
        if (brandOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Brand brand = new Brand(UUID.randomUUID(), command.name());
        brandRepository.save(brand);
    }

    @Transactional
    public void updateBrand(String id, BrandCommand command) {
        Optional<Brand> brandByIdOptional = brandRepository.findById(UUID.fromString(id));
        if (brandByIdOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Brand brandById = brandByIdOptional.get();

        Optional<Brand> brandByNameOptional = brandRepository.findByName(command.name());
        if (
                brandByNameOptional.isPresent() &&
                !brandByNameOptional.get().getId().equals(brandById.getId())
        ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        brandById.setName(command.name());

        brandRepository.save(brandById);
    }

    @Transactional
    public void deleteBrandById(String id) {
        brandRepository.deleteById(UUID.fromString(id));
    }
}