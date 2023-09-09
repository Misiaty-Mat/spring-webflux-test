package com.misiaty.demo.services;

import com.misiaty.demo.domain.Customer;
import com.misiaty.demo.mappers.CustomerMapper;
import com.misiaty.demo.mappers.CustomerMapperImpl;
import com.misiaty.demo.model.BeerDTO;
import com.misiaty.demo.model.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDto(getTestCustomer());
    }

    @Test
    void listCustomers() {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        customerService.listCustomers().subscribe(dto -> {
            atomicBoolean.set(true);
            customerDTOList.add(dto);
        });

        await().untilTrue(atomicBoolean);
        assertThat(customerDTOList.size()).isGreaterThan(1);
    }

    @Test
    void saveCustomer() {
        CustomerDTO savedDto = customerService.saveCustomer(Mono.just(getTestCustomerDto())).block();
        assertThat(savedDto).isNotNull();
        assertThat(savedDto.getId()).isNotNull();
    }

    @Test
    void getCustomerById() {
        CustomerDTO savedDto = getSavedCustomerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDto = new AtomicReference<>();
        customerService.getCustomerById(savedDto.getId()).subscribe(dto -> {
            atomicBoolean.set(true);
            atomicDto.set(dto);
        });

        await().untilTrue(atomicBoolean);
        assertThat(atomicDto.get().getId()).isEqualTo(savedDto.getId());
    }

    @Test
    void updateCustomer() {
        final String newCustomerName = "New Name";
        CustomerDTO savedCustomerDto = getSavedCustomerDto();
        savedCustomerDto.setCustomerName(newCustomerName);

        CustomerDTO updatedDto = customerService.updateCustomer(savedCustomerDto.getId(), savedCustomerDto).block();

        CustomerDTO fetchedDto = customerService.getCustomerById(updatedDto.getId()).block();
        assertThat(fetchedDto.getCustomerName()).isEqualTo(newCustomerName);
    }

    @Test
    void patchCustomer() {
        final String newCustomerName = "New Name";
        CustomerDTO savedCustomerDto = getSavedCustomerDto();
        savedCustomerDto.setCustomerName(newCustomerName);

        CustomerDTO updatedDto = customerService.patchCustomer(savedCustomerDto.getId(), savedCustomerDto).block();

        CustomerDTO fetchedDto = customerService.getCustomerById(updatedDto.getId()).block();
        assertThat(fetchedDto.getCustomerName()).isEqualTo(newCustomerName);
    }

    @Test
    void deleteCustomerById() {
        CustomerDTO customerToDelete = getSavedCustomerDto();
        customerService.deleteCustomerById(customerToDelete.getId()).block();
        Mono<CustomerDTO> expectedEmptyCustomerMono = customerService.getCustomerById(customerToDelete.getId());
        CustomerDTO emptyCustomer = expectedEmptyCustomerMono.block();

        assertThat(emptyCustomer).isNull();
    }

    public CustomerDTO getSavedCustomerDto() {
        return customerService.saveCustomer(Mono.just(getTestCustomerDto())).block();
    }

    public static CustomerDTO getTestCustomerDto() {
        return new CustomerMapperImpl().customerToCustomerDto(getTestCustomer());
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Misiaty")
                .build();
    }
}