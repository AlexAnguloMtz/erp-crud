package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.modules.products.application.query.GetBrands;
import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.BrandRepository;
import com.aram.erpcrud.modules.products.payload.BrandCommand;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.GetBrandsQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Component
public class BrandFacade {

    private final GetBrands getBrands;
    private final BrandRepository brandRepository;

    public BrandFacade(GetBrands getBrands, BrandRepository brandRepository) {
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
        Optional<Brand> byIdOptional = brandRepository.findById(id);
        if (byIdOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Brand brand = byIdOptional.get();

        Optional<Brand> byNameOptional = brandRepository.findByName(command.name());

        boolean nameConflict = byNameOptional.isPresent() &&
                (!Objects.equals(brand.getId(), byNameOptional.get().getId()));

        if (nameConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        brand.setName(command.name());

        brandRepository.save(brand);
    }

    @Transactional
    public void deleteBrandById(Long id) {
        brandRepository.deleteById(id);
    }
}