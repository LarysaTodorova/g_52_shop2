package de.aittr.g_52_shop2.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;

/*
Эта аннотация сообщает Спрингу о том, что перед нами энтити-сущность,
то есть такая сущность, для которой существует таблица в БД.
И надо объекты этого класса сопоставлять с БД.
 */
@Entity
/*
Эта аннотация сообщает Спрингу, в какой таблице в БД лежат продукты.
 */
@Table(name = "product")
public class Product {

    /*
   @Id - указываем, что именно это поле является идентификатором
   @GeneratedValue - указываем, что генерацией идентификаторов занимается сама БД
   @Column - указываем, в какой именно колонке таблицы лежат значения этого поля
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*
    Мы хотим, чтобы название продукта соответствовало требованиям:
    1. Не должно быть короче трёх символов.
    2. Не должно содержать цифры и спец.символы.
    3. Первая буква должна быть в верхнем регистре.
    4. Остальные буквы должны быть в нижнем регистре.
     */
    @Column(name = "title")
    @NotNull(message = "Product title can not be null")
    @NotBlank(message = "Product title can not be empty")
    @Pattern(regexp = "[A-Z][a-z ]{2,}",
            message = "Product title must start with capital letter and should be at least three characters length"
    )
    private String title;

    @Column(name = "price")
    @DecimalMin(value = "0.01",
            message = "Product price must be greater or equals than 0.01"
    )
    @DecimalMax(value = "1000.0",
            inclusive = false,
            message = "Product price must be less than 1000"
    )
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    @Column(name = "image_url")
    private String imageUrl;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, active, imageUrl);
    }

    @Override
    public String toString() {
        return String.format("Product: id - %d, title - %s, price - %.2f, active - %s.",
                id, title, price, active ? "yes" : "no");
    }
}
