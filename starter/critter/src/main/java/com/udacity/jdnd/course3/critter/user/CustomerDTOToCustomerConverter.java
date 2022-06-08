package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
final class CustomerDTOToCustomerConverter implements Converter<CustomerDTO, Customer> {

    @Override
    public Customer convert(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer, "id");

        return customer;
    }

}
