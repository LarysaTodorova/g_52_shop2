package de.aittr.g_52_shop2.controller;

import de.aittr.g_52_shop2.domain.dto.CartDto;
import de.aittr.g_52_shop2.domain.dto.CustomerDto;
import de.aittr.g_52_shop2.domain.entity.Role;
import de.aittr.g_52_shop2.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository repository;


    @Value("${key.access}")
    private String accessPhrase;
    private SecretKey accessKey;
    private String adminAccessToken;

    private final String BEARER_PREFIX = "Bearer ";

    private CustomerDto testCustomer;

    @BeforeEach
    public void setUp() {
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        adminAccessToken = generateAdminAccessToken();
        testCustomer = createTestCustomer();
    }

    @Test
    @Order(1)
    public void checkRequestForAllCustomers() {

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<CustomerDto[]> response = restTemplate.exchange(
                "/customers/all", HttpMethod.GET, request, CustomerDto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody(), "Response body should not be null");

        for (CustomerDto customer : response.getBody()) {
            assertNotNull(customer.getId(), "Customer id should not be null");
            assertNotNull(customer.getName(), "Customer name should not be null");
            assertNotNull(customer.getCart(), "Customer cart should not be null");
        }
    }

    @Test
    @Order(2)
    public void checkSuccessWhileSavingCustomerWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<CustomerDto> request = new HttpEntity<>(testCustomer, headers);

        ResponseEntity<CustomerDto> response = restTemplate.exchange(
                "/customers", HttpMethod.POST, request, CustomerDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");

        CustomerDto savedCustomer = response.getBody();
        assertNotNull(savedCustomer, "Saved customer should not be null");
        assertNotNull(savedCustomer.getId(), "Saved customer id should not be null");
        assertEquals(testCustomer.getName(), savedCustomer.getName(), "Saved customer has incorrect name");
        assertNotNull(savedCustomer.getCart(), "Saved customer cart should not be null");

        repository.deleteById(savedCustomer.getId());
    }

    @Test
    @Order(3)
    public void checkSuccessWhileGettingCustomerWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<CustomerDto> response = restTemplate.exchange(
                "/customers/1", HttpMethod.GET, request, CustomerDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    private CustomerDto createTestCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setName("test customer");
        customer.setCart(new CartDto());
        return customer;
    }

    private String generateAdminAccessToken() {
        Role role = new Role();
        role.setTitle("ROLE_ADMIN");

        return BEARER_PREFIX + Jwts.builder()
                .subject("TestAdmin")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("roles", Set.of(role))
                .signWith(accessKey)
                .compact();
    }

}