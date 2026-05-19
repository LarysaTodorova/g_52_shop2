package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.dto.ProductDto;
import de.aittr.g_52_shop2.domain.entity.Product;
import de.aittr.g_52_shop2.exception_handling.exceptions.EntityNotFoundException;
import de.aittr.g_52_shop2.exception_handling.exceptions.ProductNotFoundException;
import de.aittr.g_52_shop2.exception_handling.exceptions.ProductValidationException;
import de.aittr.g_52_shop2.repository.ProductRepository;
import de.aittr.g_52_shop2.service.interfaces.ProductService;
import de.aittr.g_52_shop2.service.mapping.ProductMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    // Это объект логгера, при помощи него осуществляется логирование.
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

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
        Objects.requireNonNull(dto, "Product dto cannot be null");

            Product entity = mappingService.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {

        // При помощи разных методов объекта логгера мы можем фиксировать
        // события, происходящие в программе на разные уровни
//        logger.info("Request for all products received.");
//        logger.warn("Request for all products received.");
//        logger.error("Request for all products received.");

        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
//        Product product = repository.findById(id).orElse(null);
//
//        if (product == null || !product.isActive()) {
//            throw new ProductNotFoundException(id);
//        }
//        return mappingService.mapEntityToDto(product);

        Objects.requireNonNull(id, "Product id cannot be null");

        return mappingService.mapEntityToDto(
                repository.findById(id)
                        .filter(Product::isActive)
                        .orElseThrow(() -> new EntityNotFoundException(Product.class, id))
        );
    }

    // Аннотация @Transactional служит для того, чтобы транзакция,
    // открытая в БД, действовала на протяжении всей работы метода.
    // Таким образом мы сохраняем наш продукт в состоянии managed,
    // и все изменения, которые мы вносим в этот Джава-объект,
    // автоматически попадают в БД согласно концепции ORM.
    @Override
    @Transactional
    public void update(ProductDto product) {
        Objects.requireNonNull(product, "Product cannot be null");

        Long id = product.getId();
        Product existedProduct = repository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        existedProduct.setPrice(product.getPrice());
    }

    @Override
    public void deleteById(Long id) {

        Objects.requireNonNull(id, "Product id cannot be null");

        repository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id))
                .setActive(false);
    }

    @Override
    public void deleteByTitle(String title) {
        Objects.requireNonNull(title, "Product title cannot be null");

        repository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Product with title " + title + " not found"))
                .setActive(false);
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
