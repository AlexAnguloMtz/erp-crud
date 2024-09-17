package com.aram.erpcrud.data;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.domain.StateRepository;
import com.aram.erpcrud.movements.domain.*;
import com.aram.erpcrud.products.domain.*;
import com.aram.erpcrud.users.domain.UserAddress;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
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
        List<UUID> accountIds = authUsers.stream().map(AuthUser::getId).toList();
        personalDetailsRepository.saveAll(personalDetails(accountIds));

        List<Brand> brands = brandRepository.saveAll(brands());
        List<ProductCategory> categories = productCategoryRepository.saveAll(productCategories());

        Map<String, Brand> brandMap = brands.stream().collect(Collectors.toMap(Brand::getName, Function.identity()));
        Map<String, ProductCategory> categoryMap = categories.stream().collect(Collectors.toMap(ProductCategory::getName, Function.identity()));

        List<Product> products = products(brandMap, categoryMap);
        productRepository.saveAll(products);

        movementRepository.saveAll(movements(accountIds));
    }

    private List<AuthUser> authUsers() {
        List<AuthRole> roles = authRoleRepository.findAll();
        Set<String> emails = uniqueElements(100, () -> faker.internet().emailAddress());
        return emails.stream()
                .map(email -> new AuthUser(
                        UUID.randomUUID(),
                        pickRandom(roles),
                        email,
                        passwordEncoder.encode(faker.internet().password())))
                .toList();
    }

    private <T> Set<T> uniqueElements(int amount, Supplier<T> supplier) {
        Set<T> elements = new HashSet<>();
        while (elements.size() < amount) {
            T nextElement = supplier.get();
            elements.add(nextElement);
        }
        return elements;
    }

    private Iterable<PersonalDetails> personalDetails(List<UUID> accountIds) {
        List<State> states = stateRepository.findAll();

        return accountIds.stream()
                .map(accountId -> {
                    State state = pickRandom(states);

                    UserAddress address = UserAddress.builder()
                            .id(UUID.randomUUID())
                            .stateId(state.getId())
                            .city(pickRandom(stateCitiesMap().get(state.getId())))
                            .district(faker.address().secondaryAddress())
                            .street(faker.address().streetName())
                            .streetNumber(faker.address().streetAddressNumber())
                            .zipCode(faker.address().zipCode())
                            .build();

                    return PersonalDetails.builder()
                            .id(UUID.randomUUID())
                            .accountId(accountId)
                            .name(faker.name().firstName())
                            .lastName(faker.name().lastName())
                            .phone(randomDigitsString(10))
                            .address(address)
                            .build();
                    })
                .toList();
    }

    private Iterable<Brand> brands() {
        return List.of(
                new Brand(UUID.randomUUID(), "Bimbo"),
                new Brand(UUID.randomUUID(), "Lala"),
                new Brand(UUID.randomUUID(), "Maruchan"),
                new Brand(UUID.randomUUID(), "Sabritas"),
                new Brand(UUID.randomUUID(), "Herdez"),
                new Brand(UUID.randomUUID(), "Ricolino"),
                new Brand(UUID.randomUUID(), "La Costeña"),
                new Brand(UUID.randomUUID(), "Del Fuerte"),
                new Brand(UUID.randomUUID(), "Maseca"),
                new Brand(UUID.randomUUID(), "Tortillas La Comadre"),
                new Brand(UUID.randomUUID(), "Bonafont"),
                new Brand(UUID.randomUUID(), "Jumex"),
                new Brand(UUID.randomUUID(), "Frescolita"),
                new Brand(UUID.randomUUID(), "Peñafiel"),
                new Brand(UUID.randomUUID(), "Zucaritas"),
                new Brand(UUID.randomUUID(), "Kellogg's"),
                new Brand(UUID.randomUUID(), "San Luis"),
                new Brand(UUID.randomUUID(), "Alpura"),
                new Brand(UUID.randomUUID(), "Diconsa"),
                new Brand(UUID.randomUUID(), "La Moderna"),
                new Brand(UUID.randomUUID(), "El Yucateco"),
                new Brand(UUID.randomUUID(), "Bachoco"),
                new Brand(UUID.randomUUID(), "Goya"),
                new Brand(UUID.randomUUID(), "El Costeño"),
                new Brand(UUID.randomUUID(), "Lomito"),
                new Brand(UUID.randomUUID(), "Kirkland"),
                new Brand(UUID.randomUUID(), "Clorox"),
                new Brand(UUID.randomUUID(), "Salvo"),
                new Brand(UUID.randomUUID(), "Lysol"),
                new Brand(UUID.randomUUID(), "Huggies"),
                new Brand(UUID.randomUUID(), "Pampers")
        );
    }

    private List<ProductCategory> productCategories() {
        return List.of(
                new ProductCategory(UUID.randomUUID(), "Panadería"),
                new ProductCategory(UUID.randomUUID(), "Lácteos"),
                new ProductCategory(UUID.randomUUID(), "Carnes y pescado"),
                new ProductCategory(UUID.randomUUID(), "Frutas y verduras"),
                new ProductCategory(UUID.randomUUID(), "Abarrotes secos"),
                new ProductCategory(UUID.randomUUID(), "Conservas y salsas"),
                new ProductCategory(UUID.randomUUID(), "Bebidas"),
                new ProductCategory(UUID.randomUUID(), "Snacks y golosinas"),
                new ProductCategory(UUID.randomUUID(), "Limpieza"),
                new ProductCategory(UUID.randomUUID(), "Cuidado personal"),
                new ProductCategory(UUID.randomUUID(), "Para bebés"),
                new ProductCategory(UUID.randomUUID(), "Especias y condimentos")
        );
    }

    private List<Product> products(Map<String, Brand> brandMap, Map<String, ProductCategory> categoryMap) {
        return List.of(
                // Panadería
                new Product(UUID.randomUUID(), "Pan de Caja", brandMap.get("Bimbo"), categoryMap.get("Panadería")),
                new Product(UUID.randomUUID(), "Tortillas", brandMap.get("Tortillas La Comadre"), categoryMap.get("Panadería")),
                new Product(UUID.randomUUID(), "Pan Integral", brandMap.get("Bimbo"), categoryMap.get("Panadería")),
                new Product(UUID.randomUUID(), "Pan de Hot Dog", brandMap.get("Bimbo"), categoryMap.get("Panadería")),
                new Product(UUID.randomUUID(), "Baguette", brandMap.get("Bimbo"), categoryMap.get("Panadería")),

                // Lácteos
                new Product(UUID.randomUUID(), "Leche 1L", brandMap.get("Lala"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Yogurt Natural 1L", brandMap.get("Alpura"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Crema 500 ml", brandMap.get("Lala"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Queso Oaxaca 345 grs", brandMap.get("Alpura"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Leche Condensada 250 ml", brandMap.get("Diconsa"), categoryMap.get("Lácteos")),

                // Carnes y pescado
                new Product(UUID.randomUUID(), "Pechuga de Pollo", brandMap.get("Bachoco"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Filete de Tilapia", brandMap.get("San Luis"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Costillas de Cerdo", brandMap.get("Bachoco"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Carne Molida", brandMap.get("San Luis"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Salmón Fresco", brandMap.get("Goya"), categoryMap.get("Carnes y pescado")),

                // Frutas y verduras
                new Product(UUID.randomUUID(), "Manzanas Nacionales", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Zanahorias Nacionales", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Plátanos", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Tomates Nacionales", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Papas", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),

                // Abarrotes secos
                new Product(UUID.randomUUID(), "Arroz 1 KG", brandMap.get("El Costeño"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Harina de Maíz 1KG", brandMap.get("Maseca"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Cereal 850 gr", brandMap.get("Zucaritas"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Pasta 150 gr", brandMap.get("La Moderna"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Sal de Mesa 200 gr", brandMap.get("La Costeña"), categoryMap.get("Abarrotes secos")),

                // Conservas y salsas
                new Product(UUID.randomUUID(), "Salsa Verde", brandMap.get("Herdez"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Jugo de Tomate 175 GR", brandMap.get("Del Fuerte"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Chiles en Vinagre", brandMap.get("La Costeña"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Frijoles 1 KG", brandMap.get("La Costeña"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Salsa Taquera", brandMap.get("El Yucateco"), categoryMap.get("Conservas y salsas")),

                // Bebidas
                new Product(UUID.randomUUID(), "Agua 1L", brandMap.get("Bonafont"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Jugo de Mango", brandMap.get("Jumex"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Refresco", brandMap.get("Frescolita"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Cerveza 355 ml", brandMap.get("Goya"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Té", brandMap.get("Peñafiel"), categoryMap.get("Bebidas")),

                // Snacks y golosinas
                new Product(UUID.randomUUID(), "Sabritas", brandMap.get("Sabritas"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Gansito", brandMap.get("Ricolino"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Chocorroles", brandMap.get("Ricolino"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Palomitas de Maíz", brandMap.get("Sabritas"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Tostitos", brandMap.get("Sabritas"), categoryMap.get("Snacks y golosinas")),

                // Limpieza
                new Product(UUID.randomUUID(), "Jabón 1L", brandMap.get("Lomito"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Mr. Músculo 1L", brandMap.get("Lomito"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Suavizante 1L", brandMap.get("Clorox"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Cloro 1L", brandMap.get("Lysol"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Jabón Trastes", brandMap.get("Clorox"), categoryMap.get("Limpieza")),

                // Cuidado personal
                new Product(UUID.randomUUID(), "Shampoo", brandMap.get("Kirkland"), categoryMap.get("Cuidado personal")),
                new Product(UUID.randomUUID(), "Acondicionador", brandMap.get("Kirkland"), categoryMap.get("Cuidado personal")),
                new Product(UUID.randomUUID(), "Jabón de Baño", brandMap.get("Kirkland"), categoryMap.get("Cuidado personal")),
                new Product(UUID.randomUUID(), "Pasta Dental", brandMap.get("Kirkland"), categoryMap.get("Cuidado personal")),

                // Para bebés
                new Product(UUID.randomUUID(), "Pañales Talla M", brandMap.get("Huggies"), categoryMap.get("Para bebés")),
                new Product(UUID.randomUUID(), "Leche en Polvo", brandMap.get("Pampers"), categoryMap.get("Para bebés")),
                new Product(UUID.randomUUID(), "Toallitas Húmedas", brandMap.get("Pampers"), categoryMap.get("Para bebés")),
                new Product(UUID.randomUUID(), "Crema para Bebé", brandMap.get("Huggies"), categoryMap.get("Para bebés")),

                // Especias y condimentos
                new Product(UUID.randomUUID(), "Sal de Ajo", brandMap.get("La Costeña"), categoryMap.get("Especias y condimentos")),
                new Product(UUID.randomUUID(), "Pimienta Negra", brandMap.get("La Costeña"), categoryMap.get("Especias y condimentos")),
                new Product(UUID.randomUUID(), "Comino", brandMap.get("La Costeña"), categoryMap.get("Especias y condimentos")),
                new Product(UUID.randomUUID(), "Pimentón", brandMap.get("La Costeña"), categoryMap.get("Especias y condimentos")),
                new Product(UUID.randomUUID(), "Orégano", brandMap.get("La Costeña"), categoryMap.get("Especias y condimentos")),

                // Panadería (more)
                new Product(UUID.randomUUID(), "Pan de Molde", brandMap.get("Bimbo"), categoryMap.get("Panadería")),
                new Product(UUID.randomUUID(), "Panecillos", brandMap.get("Bimbo"), categoryMap.get("Panadería")),
                new Product(UUID.randomUUID(), "Pan de Hot Dog Integral", brandMap.get("Bimbo"), categoryMap.get("Panadería")),

                // Lácteos (more)
                new Product(UUID.randomUUID(), "Leche 500 ml", brandMap.get("Lala"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Yogurt de Frutas 1L", brandMap.get("Alpura"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Mantequilla 200 grs", brandMap.get("Diconsa"), categoryMap.get("Lácteos")),
                new Product(UUID.randomUUID(), "Queso Fresco 250 grs", brandMap.get("Diconsa"), categoryMap.get("Lácteos")),

                // Carnes y pescado (more)
                new Product(UUID.randomUUID(), "Pechuga de Pavo", brandMap.get("Bachoco"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Filete de Merluza", brandMap.get("San Luis"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Chuletas de Cerdo", brandMap.get("Bachoco"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Carne de Res para Asar", brandMap.get("San Luis"), categoryMap.get("Carnes y pescado")),
                new Product(UUID.randomUUID(), "Tacos de Pescado", brandMap.get("Goya"), categoryMap.get("Carnes y pescado")),

                // Frutas y verduras (more)
                new Product(UUID.randomUUID(), "Uva Roja", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Peras", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Pepino", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Cebolla", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),
                new Product(UUID.randomUUID(), "Pimiento Morrón", brandMap.get("Lomito"), categoryMap.get("Frutas y verduras")),

                // Abarrotes secos (more)
                new Product(UUID.randomUUID(), "Galletas de Animalitos", brandMap.get("Zucaritas"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Azúcar 1 KG", brandMap.get("El Costeño"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Lentejas 500 gr", brandMap.get("El Costeño"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Harina de Trigo 1 KG", brandMap.get("Maseca"), categoryMap.get("Abarrotes secos")),
                new Product(UUID.randomUUID(), "Sopa Instantánea", brandMap.get("El Costeño"), categoryMap.get("Abarrotes secos")),

                // Conservas y salsas (more)
                new Product(UUID.randomUUID(), "Salsa Roja", brandMap.get("Herdez"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Atún en Lata", brandMap.get("Del Fuerte"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Aceitunas", brandMap.get("La Costeña"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Salsa Barbacoa", brandMap.get("El Yucateco"), categoryMap.get("Conservas y salsas")),
                new Product(UUID.randomUUID(), "Chiles Secos", brandMap.get("Herdez"), categoryMap.get("Conservas y salsas")),

                // Bebidas (more)
                new Product(UUID.randomUUID(), "Agua Mineral", brandMap.get("Bonafont"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Jugo de Naranja", brandMap.get("Jumex"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Refresco de Cola", brandMap.get("Frescolita"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Cerveza Light", brandMap.get("Goya"), categoryMap.get("Bebidas")),
                new Product(UUID.randomUUID(), "Té Helado", brandMap.get("Peñafiel"), categoryMap.get("Bebidas")),

                // Snacks y golosinas (more)
                new Product(UUID.randomUUID(), "Nachos", brandMap.get("Sabritas"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Dulces de Leche", brandMap.get("Ricolino"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Chicles", brandMap.get("Ricolino"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Galletas Saladas", brandMap.get("Sabritas"), categoryMap.get("Snacks y golosinas")),
                new Product(UUID.randomUUID(), "Barritas de Chocolate", brandMap.get("Ricolino"), categoryMap.get("Snacks y golosinas")),

                // Limpieza (more)
                new Product(UUID.randomUUID(), "Detergente Líquido", brandMap.get("Lomito"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Esponjas de Cocina", brandMap.get("Lomito"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Limpiador Multiusos", brandMap.get("Clorox"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Jabón Líquido", brandMap.get("Clorox"), categoryMap.get("Limpieza")),
                new Product(UUID.randomUUID(), "Desinfectante", brandMap.get("Lysol"), categoryMap.get("Limpieza"))
        );
    }

    private Iterable<Movement> movements(List<UUID> accountIds) {
        List<MovementType> movementTypes = movementTypeRepository.findAll();
        List<Product> products = productRepository.findAll();
        return IntStream.range(0, 100)
                .mapToObj(i -> {

                    List<StockMovementProduct> productQuantities = randomProductQuantities(products);

                    Movement movement = new Movement(
                        UUID.randomUUID(),
                        pickRandom(accountIds),
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

    private Map<String, List<String>> stateCitiesMap() {
        Map<String, List<String>> stateCities = new HashMap<>();

        stateCities.put("AGU", Arrays.asList("Aguascalientes", "San Francisco de los Romo", "Jesús María"));
        stateCities.put("BCN", Arrays.asList("Tijuana", "Mexicali", "Ensenada"));
        stateCities.put("BCS", Arrays.asList("La Paz", "Cabo San Lucas", "San José del Cabo"));
        stateCities.put("CAM", Arrays.asList("San Francisco de Campeche", "Ciudad del Carmen", "Champotón"));
        stateCities.put("COA", Arrays.asList("Saltillo", "Torreón", "Monclova"));
        stateCities.put("COL", Arrays.asList("Colima", "Manzanillo", "Tecomán"));
        stateCities.put("CHI", Arrays.asList("Tuxtla Gutiérrez", "San Cristóbal de las Casas", "Tapachula"));
        stateCities.put("CHH", Arrays.asList("Chihuahua", "Ciudad Juárez", "Delicias"));
        stateCities.put("CDMX", Arrays.asList("Ciudad de México", "Coyoacán", "Gustavo A. Madero"));
        stateCities.put("DUR", Arrays.asList("Durango", "Gómez Palacio", "Lerdo"));
        stateCities.put("GUA", Arrays.asList("León", "Guanajuato", "Irapuato"));
        stateCities.put("GUE", Arrays.asList("Acapulco", "Chilpancingo", "Iguala"));
        stateCities.put("HID", Arrays.asList("Pachuca", "Tulancingo", "Tizayuca"));
        stateCities.put("JAL", Arrays.asList("Guadalajara", "Puerto Vallarta", "Tlaquepaque"));
        stateCities.put("MEX", Arrays.asList("Toluca", "Naucalpan", "Ecatepec"));
        stateCities.put("MIC", Arrays.asList("Morelia", "Uruapan", "Zamora"));
        stateCities.put("MOR", Arrays.asList("Cuernavaca", "Jiutepec", "Temixco"));
        stateCities.put("NAY", Arrays.asList("Tepic", "Bahía de Banderas", "Xalisco"));
        stateCities.put("NLE", Arrays.asList("Monterrey", "San Pedro Garza García", "Santa Catarina"));
        stateCities.put("OAX", Arrays.asList("Oaxaca de Juárez", "Santa Cruz Xoxocotlán", "San Bartolo Coyotepec"));
        stateCities.put("PUE", Arrays.asList("Puebla", "Atlixco", "San Andrés Cholula"));
        stateCities.put("QUE", Arrays.asList("Querétaro", "El Marqués", "San Juan del Río"));
        stateCities.put("ROO", Arrays.asList("Cancún", "Playa del Carmen", "Chetumal"));
        stateCities.put("SLP", Arrays.asList("San Luis Potosí", "Soledad de Graciano Sánchez", "Matehuala"));
        stateCities.put("SIN", Arrays.asList("Culiacán", "Mazatlán", "Los Mochis"));
        stateCities.put("SON", Arrays.asList("Hermosillo", "Ciudad Obregón", "Nogales"));
        stateCities.put("TAB", Arrays.asList("Villahermosa", "Comalcalco", "Cárdenas"));
        stateCities.put("TAM", Arrays.asList("Reynosa", "Matamoros", "Tampico"));
        stateCities.put("TLA", Arrays.asList("Tlaxcala", "Apizaco", "San Pablo del Monte"));
        stateCities.put("VER", Arrays.asList("Xalapa", "Veracruz", "Coatzacoalcos"));
        stateCities.put("YUC", Arrays.asList("Mérida", "Progreso", "Valladolid"));
        stateCities.put("ZAC", Arrays.asList("Zacatecas", "Guadalupe", "Jerez"));

        return stateCities;
    }

    private List<StockMovementProduct> randomProductQuantities(List<Product> products) {
        List<UUID> seenProductIds = new ArrayList<>();
        return IntStream.range(1, new Random().nextInt(5, 10))
                .mapToObj(_ -> randomProductQuantity(products, seenProductIds))
                .toList();
    }

    private StockMovementProduct randomProductQuantity(List<Product> products, List<UUID> seenProductIds) {
        while (true) {
            Product product = pickRandom(products);
            if (!seenProductIds.contains(product.getId())) {
                seenProductIds.add(product.getId());
                return new StockMovementProduct(
                    UUID.randomUUID(),
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

    private String randomDigitsString(int length) {
        String DIGITS = "123456789";

        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(index));
        }

        return sb.toString();
    }
}