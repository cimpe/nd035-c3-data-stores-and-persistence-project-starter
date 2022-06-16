package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetNotFoundException;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    private final CustomerDTOToCustomerConverter customerDTOToCustomerConverter;

    private final CustomerToCustomerDTOConverter customerToCustomerDTOConverter;

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerToCustomerDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public CustomerDTO save(final CustomerDTO customer) {
        final Customer toSaveCustomer = customerDTOToCustomerConverter.convert(customer);

        if (toSaveCustomer.getId() != null) {
            customerRepository.findById(toSaveCustomer.getId())
                    .orElseThrow(CustomerNotFoundException::new);
        }

        final Customer savedCustomer = customerRepository.save(toSaveCustomer);

        return customerToCustomerDTOConverter.convert(savedCustomer);
    }

    public CustomerDTO getOwnerByPet(final long petId) {
        final Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);

        final Customer owner = pet.getOwner();
        if (owner != null) {
            return customerToCustomerDTOConverter.convert(owner);
        }

        return null;
    }

}
