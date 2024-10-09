package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.modules.products.application.query.GetBrandsQueryHandler;
import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.BrandRepository;
import com.aram.erpcrud.modules.products.payload.BrandCommand;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.GetBrandsQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

        Brand brand = new Brand();
        brand.setName(command.name());

        brandRepository.save(brand);
    }

    @Transactional
    public void updateBrand(Long id, BrandCommand command) {
        Optional<Brand> brandByIdOptional = brandRepository.findById(id);
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
    public void deleteBrandById(Long id) {
        brandRepository.deleteById(id);
    }
}