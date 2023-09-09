package com.misiaty.demo.services;

import com.misiaty.demo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> listCustomers();
    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDto);
    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDto);
    Mono<CustomerDTO> getCustomerById(String customerId);
    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDto);
    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDto);
    Mono<Void> deleteCustomerById(String customerId);
}
