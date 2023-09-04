package com.template.project.repository;

import com.template.project.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        //given
        Customer customer = new Customer();
        customer.setChatId(1234567L);
        customer.setUsername("@JohnDoe");

        // when
        Customer savedCustomer = customerRepository.save(customer);

        // then
        assertNotNull(savedCustomer.getChatId());
        Optional<Customer> retrievedCustomer = customerRepository.findById(savedCustomer.getChatId());
        assertTrue(retrievedCustomer.isPresent());
        assertEquals("@JohnDoe", retrievedCustomer.get().getUsername());
    }

}