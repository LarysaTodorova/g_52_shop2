package de.aittr.g_52_shop2.controller;

import de.aittr.g_52_shop2.domain.dto.ProductDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest - при старте тестов запускает наше приложение
// полноценно на тестовом экземпляре Tomcat
// webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT -
// этот атрибут говорит о том, что тестовый экземпляр Tomcat с нашим
// приложением должен подняться на случайно выбранном свободном порту
// нашей операционной системы
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// В интеграционных тестах иногда важен порядок запуска тестов.
// Если нам важен порядок, мы должны включить соответствующую настройку.
// И аннотация @TestMethodOrder(MethodOrderer.OrderAnnotation.class) говорит
// о том, что мы будем запускать методы в определённом порядке,
// регулируя это при помощи другой аннотации, которая будет стоять
// на наших тестовых методах
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTestIT {

    // Аннотация @LocalServerPort позволяет в это поле
    // сохранить значение случайно выбранного порта, на котором
    // стартовал тестовый Tomcat
    @LocalServerPort
    private int port;

    // TestRestTemplate - это такой объект, при помощи которого мы
    // можем отправлять реальные http-запросы на REST-контроллер
    // нашего приложения и получать http-ответы от него.
    // Аннотация @Autowired говорит фреймворку о том, что в это
    // поле нужно автоматически внедрить нужный объект типа TestRestTemplate
    @Autowired
    private TestRestTemplate restTemplate;

    // Аннотация @Test говорит фреймворку о том, что это именно тестовый метод,
    // и его нужно запускать как тест
    @Test
    // Аннотация @Order(1) говорит о том, что этот метод нужно запустить
    // первым по счёту
    @Order(1)
    public void checkRequestForAllProducts() {
        // Здесь мы создаём заголовки http-запроса.
        // Пока нам нечего в них добавлять, они будут просто пустые.
        HttpHeaders headers = new HttpHeaders();

        // Здесь мы создаём объект http-запроса, передавая ему
        // в конструктор объект заголовков.
        // При этом запрос мы параметризуем типом Void, что говорит
        // о том, что мы ничего не собираемся отправлять в качестве
        // тела запроса.
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Здесь мы отправляем на наше тестовое приложение реальный http-запрос
        // и получаем реальный ответ. Для этого в метод exchange отправляем
        // четыре аргумента - эндпоинт, на который обращаемся, тип запроса,
        // сам объект запроса, а так же класс, объекты которого ожидаем получить в теле ответа.
        ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                "/products/all", HttpMethod.GET, request, ProductDto[].class
        );

        // Здесь мы проверяем, действительно ли от сервера пришёл тот статус ответа, который мы ждём.
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status code");

        // Здесь мы проверяем, что тело ответа не пустое.
        // Даже если в БД нет ни одного продукта - мы ожидаем просто пустой лист.
        // Пустой лист - это объект, он не null.
        assertNotNull(response.getBody(), "Response body should not be null");
    }
}