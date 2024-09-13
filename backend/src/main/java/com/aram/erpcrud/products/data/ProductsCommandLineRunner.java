package com.aram.erpcrud.products.data;

import com.aram.erpcrud.products.domain.Brand;
import com.aram.erpcrud.products.domain.BrandRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ProductsCommandLineRunner implements CommandLineRunner {

    private final BrandRepository brandRepository;

    ProductsCommandLineRunner(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        brandRepository.saveAll(brands());
    }

    private Iterable<Brand> brands() {
        return List.of(
            new Brand("1", "Bimbo"),
            new Brand("2", "Lala"),
            new Brand("3", "Maruchan"),
            new Brand("4", "Sabritas"),
            new Brand("5", "Herdez"),
            new Brand("6", "Ricolino"),
            new Brand("7", "La Costeña"),
            new Brand("8", "Del Fuerte"),
            new Brand("9", "Maseca"),
            new Brand("10", "Tortillas La Comadre"),
            new Brand("11", "Bonafont"),
            new Brand("12", "Jumex"),
            new Brand("13", "Frescolita"),
            new Brand("14", "Gansito"),
            new Brand("15", "Nabisco"),
            new Brand("16", "Peñafiel"),
            new Brand("17", "Zucaritas"),
            new Brand("18", "Kellogg's"),
            new Brand("19", "San Luis"),
            new Brand("20", "Alpura"),
            new Brand("21", "Diconsa"),
            new Brand("22", "Productos Rivas"),
            new Brand("23", "La Moderna"),
            new Brand("24", "El Yucateco"),
            new Brand("25", "Goya"),
            new Brand("26", "Bachoco"),
            new Brand("27", "Goya"),
            new Brand("28", "El Costeño"),
            new Brand("29", "Minsa"),
            new Brand("30", "Lomito")
        );
    }
}