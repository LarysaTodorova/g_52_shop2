package de.aittr.g_52_shop2.controller;

import de.aittr.g_52_shop2.domain.entity.Customer;
import de.aittr.g_52_shop2.service.interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    //  Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return service.save(customer);
    }

    // Вернуть всех покупателей из базы данных (активных).
    @GetMapping("/all")
    public List<Customer> getAll() {
        return service.findAll();
    }

    //  Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return service.findById(id);
    }

    //  Изменить одного покупателя в базе данных по его идентификатору.
    @PutMapping
    public void update(@RequestBody Customer customer) {
        service.update(customer);
    }

    //  Удалить покупателя из базы данных по его идентификатору.
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    //  Удалить покупателя из базы данных по его имени.
//    @DeleteMapping("/by-name/{name}")
//    public void deleteByName(@PathVariable String name) {
//    }
    @DeleteMapping
    public void deleteByName(@RequestParam String name) {
        service.deleteByName(name);
    }

    //  Восстановить удалённого покупателя в базе данных по его идентификатору.
    @PutMapping("/restore/{id}")
    public void restoreById(@PathVariable Long id) {
        service.restoreById(id);
    }

    //  Вернуть общее количество покупателей в базе данных (активных).
    @GetMapping("/quantity")
    public long getCustomersQuantity() {
        return service.getAllActiveCustomersCount();
    }

//  Вернуть стоимость корзины покупателя по его идентификатору (если он активен).
//  Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
//  Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
//  Удалить товар из корзины покупателя по их идентификаторам
//  Полностью очистить корзину покупателя по его идентификатору (если он активен)
}
