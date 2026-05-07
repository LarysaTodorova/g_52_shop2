package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.dto.ProductDto;
import de.aittr.g_52_shop2.domain.entity.Product;
import de.aittr.g_52_shop2.repository.ProductRepository;
import de.aittr.g_52_shop2.service.interfaces.ProductService;
import de.aittr.g_52_shop2.service.mapping.ProductMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/*
Аннотация @Service говорит Спрингу о том, что на старте приложения
нужно создать объект этого класса и поместить его в Спринг контекст.
Кроме того, данная аннотация носит информационный характер,
она говорит нам о том, что перед нами класс сервиса.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMappingService mappingService;

    /*
    Когда Спринг будет создавать объект сервиса продуктов, он вызовет
    этот конструктор (потому что вариантов других нет), а в этот конструктор
    требуется передать объект репозитория. Поэтому Спринг обратится в
    контекст, достанет оттуда репозиторий и передаст в этот параметр.
    А объект репозитория там уже будет находиться благодаря наследованию
    нашего интерфейса репозитория от JpaRepository.
     */
    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public ProductDto save(ProductDto dto) {
        Product entity = mappingService.mapDtoToEntity(dto);
        entity = repository.save(entity);
        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = repository.findById(id).orElse(null);

        if (product == null || !product.isActive()) {
            return null;
        }
        return mappingService.mapEntityToDto(product);
    }

    // Аннотация @Transactional служит для того, чтобы транзакция,
    // открытая в БД, действовала на протяжении всей работы метода.
    // Таким образом мы сохраняем наш продукт в состоянии managed,
    // и все изменения, которые мы вносим в этот Джава-объект,
    // автоматически попадают в БД согласно концепции ORM.
    @Override
    @Transactional
    public void update(ProductDto product) {
        Long id = product.getId();
        Product existedProduct = repository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
        existedProduct.setPrice(product.getPrice());
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByTitle(String title) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveProductsCount() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .count();
    }

    @Override
    public BigDecimal getAllActiveTotalCost() {
        return getAllActiveProducts()
                .stream()
                .map(ProductDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getAllActiveProductsAveragePrice() {
        return null;
    }
}
