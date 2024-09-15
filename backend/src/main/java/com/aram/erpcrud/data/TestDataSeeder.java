package com.aram.erpcrud.data;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.domain.StateRepository;
import com.aram.erpcrud.movements.domain.*;
import com.aram.erpcrud.products.domain.*;
import com.aram.erpcrud.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.personaldetails.domain.PersonalDetailsRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class TestDataSeeder {

    private final Faker faker;
    private final AuthRoleRepository authRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final BrandRepository brandRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;
    private final MovementTypeRepository movementTypeRepository;
    private final StateRepository stateRepository;

    TestDataSeeder(
            AuthRoleRepository authRoleRepository,
            PasswordEncoder passwordEncoder,
            AuthUserRepository authUserRepository,
            PersonalDetailsRepository personalDetailsRepository,
            BrandRepository brandRepository,
            ProductCategoryRepository productCategoryRepository,
            ProductRepository productRepository,
            MovementRepository movementRepository,
            MovementTypeRepository movementTypeRepository, StateRepository stateRepository
    ) {
        this.authRoleRepository = authRoleRepository;
        this.movementTypeRepository = movementTypeRepository;
        this.stateRepository = stateRepository;
        this.faker = new Faker(new Locale("es"), new RandomService());
        this.passwordEncoder = passwordEncoder;
        this.authUserRepository = authUserRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.brandRepository = brandRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    public void seed() throws Exception {
        List<AuthUser> authUsers = authUserRepository.saveAll(authUsers());
        List<String> accountIds = authUsers.stream().map(AuthUser::getId).toList();
        personalDetailsRepository.saveAll(personalDetails(accountIds));

        List<Brand> brands = brandRepository.saveAll(brands());
        List<ProductCategory> categories = productCategoryRepository.saveAll(productCategories());

        Map<String, Brand> brandMap = brands.stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
        Map<String, ProductCategory> categoryMap = categories.stream().collect(Collectors.toMap(ProductCategory::getId, Function.identity()));

        List<Product> products = products(brandMap, categoryMap);
        productRepository.saveAll(products);

        movementRepository.saveAll(movements());
    }

    private List<AuthUser> authUsers() {
        List<AuthRole> roles = authRoleRepository.findAll();

        return IntStream.range(0, 100)
                .mapToObj(i -> new AuthUser(String.valueOf(i), pickRandom(roles), faker.internet().emailAddress(), passwordEncoder.encode(faker.internet().password())))
                .toList();
    }

    private Iterable<PersonalDetails> personalDetails(List<String> accountIds) {
        List<State> states = stateRepository.findAll();
        return accountIds.stream()
                .map(accountId -> new PersonalDetails(
                        UUID.randomUUID().toString(),
                        accountId,
                        faker.name().firstName(),
                        faker.name().lastName(),
                        pickRandom(states),
                        faker.address().city(),
                        faker.address().secondaryAddress(),
                        faker.address().streetName(),
                        faker.address().streetAddressNumber(),
                        digitsOnly(faker.phoneNumber().phoneNumber()),
                        faker.address().zipCode()
                ))
                .toList();
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
                new Brand("30", "Lomito"),
                new Brand("32", "Kirkland"),
                new Brand("33", "Clorox"),
                new Brand("34", "Salvo"),
                new Brand("35", "Lysol"),
                new Brand("36", "Huggies"),
                new Brand("37", "Pampers")
        );
    }

    private List<ProductCategory> productCategories() {
        return List.of(
                new ProductCategory("1", "Panadería"),
                new ProductCategory("2", "Lácteos"),
                new ProductCategory("3", "Carnes y pescado"),
                new ProductCategory("4", "Frutas y verduras"),
                new ProductCategory("5", "Abarrotes secos"),
                new ProductCategory("6", "Conservas y salsas"),
                new ProductCategory("7", "Bebidas"),
                new ProductCategory("8", "Snacks y golosinas"),
                new ProductCategory("9", "Limpieza"),
                new ProductCategory("10", "Cuidado personal"),
                new ProductCategory("11", "Para bebés"),
                new ProductCategory("12", "Especias y condimentos")
        );
    }

    private List<Product> products(Map<String, Brand> brandMap, Map<String, ProductCategory> categoryMap) {
        return List.of(
                // Panadería
                new Product("1", "Pan de Caja", brandMap.get("1"), categoryMap.get("1")),
                new Product("2", "Tortillas", brandMap.get("10"), categoryMap.get("1")),
                new Product("3", "Pan Integral", brandMap.get("1"), categoryMap.get("1")),
                new Product("4", "Pan de Hot Dog", brandMap.get("1"), categoryMap.get("1")),
                new Product("5", "Baguette", brandMap.get("1"), categoryMap.get("1")),

                // Lácteos
                new Product("6", "Leche 1L", brandMap.get("2"), categoryMap.get("2")),
                new Product("7", "Yogurt Natural 1L", brandMap.get("20"), categoryMap.get("2")),
                new Product("8", "Crema 500 ml", brandMap.get("2"), categoryMap.get("2")),
                new Product("9", "Queso Oaxaca 345 grs", brandMap.get("20"), categoryMap.get("2")),
                new Product("10", "Leche Condensada 250 ml", brandMap.get("21"), categoryMap.get("2")),

                // Carnes y pescado
                new Product("11", "Pechuga de Pollo", brandMap.get("26"), categoryMap.get("3")),
                new Product("12", "Filete de Tilapia", brandMap.get("19"), categoryMap.get("3")),
                new Product("13", "Costillas de Cerdo", brandMap.get("26"), categoryMap.get("3")),
                new Product("14", "Carne Molida", brandMap.get("19"), categoryMap.get("3")),
                new Product("15", "Salmón Fresco", brandMap.get("27"), categoryMap.get("3")),

                // Frutas y verduras
                new Product("16", "Manzanas Nacionales", brandMap.get("30"), categoryMap.get("4")),
                new Product("17", "Zanahorias Nacionales", brandMap.get("30"), categoryMap.get("4")),
                new Product("18", "Plátanos", brandMap.get("30"), categoryMap.get("4")),
                new Product("19", "Tomates Nacionales", brandMap.get("30"), categoryMap.get("4")),
                new Product("20", "Papas", brandMap.get("30"), categoryMap.get("4")),

                // Abarrotes secos
                new Product("21", "Arroz 1 KG", brandMap.get("28"), categoryMap.get("5")),
                new Product("22", "Harina de Maíz 1KG", brandMap.get("9"), categoryMap.get("5")),
                new Product("23", "Cereal 850 gr", brandMap.get("17"), categoryMap.get("5")),
                new Product("24", "Pasta 150 gr", brandMap.get("23"), categoryMap.get("5")),
                new Product("25", "Sal de Mesa 200 gr", brandMap.get("7"), categoryMap.get("5")),

                // Conservas y salsas
                new Product("26", "Salsa Verde", brandMap.get("5"), categoryMap.get("6")),
                new Product("27", "Jugo de Tomate 175 GR", brandMap.get("8"), categoryMap.get("6")),
                new Product("28", "Chiles en Vinagre", brandMap.get("7"), categoryMap.get("6")),
                new Product("29", "Frijoles 1 KG", brandMap.get("7"), categoryMap.get("6")),
                new Product("30", "Salsa Taquera", brandMap.get("24"), categoryMap.get("6")),

                // Bebidas
                new Product("31", "Agua 1L", brandMap.get("11"), categoryMap.get("7")),
                new Product("32", "Jugo de Mango", brandMap.get("12"), categoryMap.get("7")),
                new Product("33", "Refresco", brandMap.get("13"), categoryMap.get("7")),
                new Product("34", "Cerveza 355 ml", brandMap.get("27"), categoryMap.get("7")),
                new Product("35", "Té", brandMap.get("16"), categoryMap.get("7")),

                // Snacks y golosinas
                new Product("36", "Sabritas", brandMap.get("4"), categoryMap.get("8")),
                new Product("37", "Gansito", brandMap.get("6"), categoryMap.get("8")),
                new Product("38", "Chocorroles", brandMap.get("6"), categoryMap.get("8")),
                new Product("39", "Palomitas de Maíz", brandMap.get("4"), categoryMap.get("8")),
                new Product("40", "Tostitos", brandMap.get("4"), categoryMap.get("8")),

                // Limpieza
                new Product("41", "Jabón 1L", brandMap.get("30"), categoryMap.get("9")),
                new Product("42", "Mr. Músculo 1L", brandMap.get("30"), categoryMap.get("9")),
                new Product("43", "Suavizante 1L", brandMap.get("33"), categoryMap.get("9")),
                new Product("44", "Cloro 1L", brandMap.get("35"), categoryMap.get("9")),
                new Product("45", "Jabón Trastes", brandMap.get("34"), categoryMap.get("9")),

                // Cuidado personal
                new Product("46", "Shampoo", brandMap.get("32"), categoryMap.get("10")),
                new Product("47", "Acondicionador", brandMap.get("32"), categoryMap.get("10")),
                new Product("48", "Jabón de Baño", brandMap.get("32"), categoryMap.get("10")),
                new Product("49", "Pasta Dental", brandMap.get("32"), categoryMap.get("10")),

                // Para bebés
                new Product("50", "Pañales Talla M", brandMap.get("36"), categoryMap.get("11")),
                new Product("51", "Leche en Polvo", brandMap.get("37"), categoryMap.get("11")),
                new Product("52", "Toallitas Húmedas", brandMap.get("37"), categoryMap.get("11")),
                new Product("53", "Crema para Bebé", brandMap.get("36"), categoryMap.get("11")),

                // Especias y condimentos
                new Product("54", "Sal de Ajo", brandMap.get("7"), categoryMap.get("12")),
                new Product("55", "Pimienta Negra", brandMap.get("7"), categoryMap.get("12")),
                new Product("56", "Comino", brandMap.get("7"), categoryMap.get("12")),
                new Product("57", "Pimentón", brandMap.get("7"), categoryMap.get("12")),
                new Product("58", "Orégano", brandMap.get("7"), categoryMap.get("12")),

                // Panadería (more)
                new Product("59", "Pan de Molde", brandMap.get("1"), categoryMap.get("1")),
                new Product("60", "Panecillos", brandMap.get("1"), categoryMap.get("1")),
                new Product("61", "Pan de Hot Dog Integral", brandMap.get("1"), categoryMap.get("1")),

                // Lácteos (more)
                new Product("62", "Leche 500 ml", brandMap.get("2"), categoryMap.get("2")),
                new Product("63", "Yogurt de Frutas 1L", brandMap.get("20"), categoryMap.get("2")),
                new Product("64", "Mantequilla 200 grs", brandMap.get("21"), categoryMap.get("2")),
                new Product("65", "Queso Fresco 250 grs", brandMap.get("21"), categoryMap.get("2")),

                // Carnes y pescado (more)
                new Product("66", "Pechuga de Pavo", brandMap.get("26"), categoryMap.get("3")),
                new Product("67", "Filete de Merluza", brandMap.get("19"), categoryMap.get("3")),
                new Product("68", "Chuletas de Cerdo", brandMap.get("26"), categoryMap.get("3")),
                new Product("69", "Carne de Res para Asar", brandMap.get("19"), categoryMap.get("3")),
                new Product("70", "Tacos de Pescado", brandMap.get("27"), categoryMap.get("3")),

                // Frutas y verduras (more)
                new Product("71", "Uva Roja", brandMap.get("30"), categoryMap.get("4")),
                new Product("72", "Peras", brandMap.get("30"), categoryMap.get("4")),
                new Product("73", "Pepino", brandMap.get("30"), categoryMap.get("4")),
                new Product("74", "Cebolla", brandMap.get("30"), categoryMap.get("4")),
                new Product("75", "Pimiento Morrón", brandMap.get("30"), categoryMap.get("4")),

                // Abarrotes secos (more)
                new Product("76", "Galletas de Animalitos", brandMap.get("17"), categoryMap.get("5")),
                new Product("77", "Azúcar 1 KG", brandMap.get("28"), categoryMap.get("5")),
                new Product("78", "Lentejas 500 gr", brandMap.get("28"), categoryMap.get("5")),
                new Product("79", "Harina de Trigo 1 KG", brandMap.get("9"), categoryMap.get("5")),
                new Product("80", "Sopa Instantánea", brandMap.get("28"), categoryMap.get("5")),

                // Conservas y salsas (more)
                new Product("81", "Salsa Roja", brandMap.get("5"), categoryMap.get("6")),
                new Product("82", "Atún en Lata", brandMap.get("8"), categoryMap.get("6")),
                new Product("83", "Aceitunas", brandMap.get("7"), categoryMap.get("6")),
                new Product("84", "Salsa Barbacoa", brandMap.get("24"), categoryMap.get("6")),
                new Product("85", "Chiles Secos", brandMap.get("5"), categoryMap.get("6")),

                // Bebidas (more)
                new Product("86", "Agua Mineral", brandMap.get("11"), categoryMap.get("7")),
                new Product("87", "Jugo de Naranja", brandMap.get("12"), categoryMap.get("7")),
                new Product("88", "Refresco de Cola", brandMap.get("13"), categoryMap.get("7")),
                new Product("89", "Cerveza Light", brandMap.get("27"), categoryMap.get("7")),
                new Product("90", "Té Helado", brandMap.get("16"), categoryMap.get("7")),

                // Snacks y golosinas (more)
                new Product("91", "Nachos", brandMap.get("4"), categoryMap.get("8")),
                new Product("92", "Dulces de Leche", brandMap.get("6"), categoryMap.get("8")),
                new Product("93", "Chicles", brandMap.get("6"), categoryMap.get("8")),
                new Product("94", "Galletas Saladas", brandMap.get("4"), categoryMap.get("8")),
                new Product("95", "Barritas de Chocolate", brandMap.get("6"), categoryMap.get("8")),

                // Limpieza (more)
                new Product("96", "Detergente Líquido", brandMap.get("30"), categoryMap.get("9")),
                new Product("97", "Esponjas de Cocina", brandMap.get("30"), categoryMap.get("9")),
                new Product("98", "Limpiador Multiusos", brandMap.get("33"), categoryMap.get("9")),
                new Product("99", "Jabón Líquido", brandMap.get("34"), categoryMap.get("9")),
                new Product("100", "Desinfectante", brandMap.get("35"), categoryMap.get("9"))
        );
    }

    private Iterable<Movement> movements() {
        List<MovementType> movementTypes = movementTypeRepository.findAll();
        List<Product> products = productRepository.findAll();
        return IntStream.range(0, 100)
                .mapToObj(i -> {

                    List<ProductQuantity> productQuantities = randomProductQuantities(products);

                    Movement movement = new Movement(
                        String.valueOf(i),
                        String.valueOf(new Random().nextInt(1, 99)),
                        productQuantities,
                        pickRandom(movementTypes),
                        pickRandom(movementObservations()),
                        randomInstantBetween(
                            Instant.parse("2024-03-10T00:00:00Z"),
                            Instant.parse("2024-09-01T00:00:00Z")
                        )
                    );

                    productQuantities.forEach(productQuantity -> productQuantity.setMovement(movement));

                    return movement;
                })
                .toList();
    }

    private List<String> movementObservations() {
        return List.of(
                "Sin observaciones",
                "Producto abierto",
                "Producto caducado",
                "Envases rotos",
                "Producto incompleto",
                "Etiqueta faltante",
                "Stock desactualizado",
                "Producto dañado durante el transporte",
                "Error en la cantidad",
                "Producto incorrecto en el pedido",
                "Falta de documentación",
                "Diferencia en el peso",
                "Producto mal almacenado",
                "Revisión necesaria",
                "Productos mezclados con otros lotes",
                "Observaciones del proveedor",
                "Retraso en la entrega",
                "Problemas con el embalaje",
                "Verificar fecha de expiración",
                "Producto mal etiquetado",
                "Faltante en la entrega",
                "Artículo con defectos visibles",
                "Precauciones adicionales necesarias"
        );
    }

    private List<ProductQuantity> randomProductQuantities(List<Product> products) {
        List<String> seenProductIds = new ArrayList<>();
        return IntStream.range(1, new Random().nextInt(5, 10))
                .mapToObj(_ -> randomProductQuantity(products, seenProductIds))
                .toList();
    }

    private ProductQuantity randomProductQuantity(List<Product> products, List<String> seenProductIds) {
        while (true) {
            Product product = pickRandom(products);
            if (!seenProductIds.contains(product.getId())) {
                seenProductIds.add(product.getId());
                return new ProductQuantity(
                    UUID.randomUUID().toString(),
                    product.getId(),
                    new Random().nextInt(1, 30),
                    null
                );
            }
        }
    }

    private <T> T pickRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    private Instant randomInstantBetween(Instant start, Instant end) {
        long startEpochSecond = start.getEpochSecond();
        long endEpochSecond = end.getEpochSecond();

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start instant must be before end instant.");
        }

        long randomEpochSecond = startEpochSecond + (long) (new Random().nextDouble() * (endEpochSecond - startEpochSecond));
        return Instant.ofEpochSecond(randomEpochSecond);
    }

    private String digitsOnly(String string) {
        return string.chars()
                .filter(Character::isDigit)
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }
}