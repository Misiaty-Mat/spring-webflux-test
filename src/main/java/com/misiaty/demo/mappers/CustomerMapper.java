package com.misiaty.demo.mappers;

import com.misiaty.demo.domain.Customer;
import com.misiaty.demo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
