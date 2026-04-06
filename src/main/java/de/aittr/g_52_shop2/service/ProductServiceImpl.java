package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.entity.Product;
import de.aittr.g_52_shop2.repository.ProductRepository;
import org.springframework.stereotype.Service;

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

    /*
    Когда Спринг будет создавать объект сервиса продуктов, он вызовет
    этот конструктор (потому что вариантов других нет), а в этот конструктор
    требуется передать объект репозитория. Поэтому Спринг обратится в
    контекст, достанет оттуда репозиторий и передаст в этот параметр.
    А объект репозитория там уже будет находиться благодаря наследованию
    нашего интерфейса репозитория от JpaRepository.
     */
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        product.setActive(true);
        return repository.save(product);
    }

    @Override
    public List<Product> getAllActiveProducts() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .toList();
    }

    @Override
    public Product getById(Long id) {
        Product product = repository.findById(id).orElse(null);

        if (product == null || !product.isActive()) {
            return null;
        }
        return product;
    }

    @Override
    public void update(Product product) {

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
        return getAllActiveProducts().size();
    }

    @Override
    public BigDecimal getAllActiveTotalCost() {
        return getAllActiveProducts()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getAllActiveProductsAveragePrice() {
        return null;
    }
}
