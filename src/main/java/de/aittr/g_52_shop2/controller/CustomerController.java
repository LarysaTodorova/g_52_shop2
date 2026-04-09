package de.aittr.g_52_shop2.controller;

import de.aittr.g_52_shop2.domain.dto.CustomerDto;
import de.aittr.g_52_shop2.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer controller", description = "Controller for various operations with Customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    //  Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    @PostMapping
    public CustomerDto save(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Customer")
            CustomerDto customer
    ) {
        return service.save(customer);
    }

    // Вернуть всех покупателей из базы данных (активных).
    @GetMapping("/all")
    @Operation(
            summary = "Get all customers",
            description = "Getting all customers that exist in the database"
    )
    public List<CustomerDto> getAll() {
        return service.findAllActiveCustomers();
    }

    //  Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    @GetMapping("/{id}")
    public CustomerDto getById(
            @PathVariable
            @Parameter(description = "Product unique identifier")
            Long id
    ) {
        return service.findById(id);
    }

    //  Изменить одного покупателя в базе данных по его идентификатору.
    @PutMapping
    public void update(@RequestBody CustomerDto customer) {
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
