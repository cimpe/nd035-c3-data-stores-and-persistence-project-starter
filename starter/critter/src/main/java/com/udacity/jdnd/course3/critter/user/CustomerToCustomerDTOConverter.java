package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
final class CustomerToCustomerDTOConverter implements Converter<Customer, CustomerDTO> {

    @Override
    public CustomerDTO convert(final Customer customer) {
        final CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);

        customerDTO.setPetIds(customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));

        return customerDTO;
    }

}
