package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    private final PetToPetDTOConverter petToPetDTOConverter;

    private final CustomerRepository customerRepository;

    public List<PetDTO> findAll() {
        return petRepository.findAll()
                .stream()
                .map(petToPetDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public PetDTO findById(final Long id) {
        return petRepository.findById(id)
                .map(petToPetDTOConverter::convert)
                .orElseThrow(PetNotFoundException::new);
    }

    public List<PetDTO> findByOwnerId(final Long ownerId) {
        final Customer customer = new Customer();
        customer.setId(ownerId);

        return petRepository.findAllByOwner(customer)
                .stream()
                .map(petToPetDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public PetDTO save(final PetDTO pet) {
        final Customer customer = customerRepository.findById(pet.getOwnerId())
                .orElseThrow(CustomerNotFoundException::new);

        final Pet toSavePet = convertPetDTOToPet(pet, customer);

        final Pet savedPet;
        if (toSavePet.getId() != null) {
            savedPet = petRepository.findById(toSavePet.getId())
                    .map(petRepository::save)
                    .orElseThrow(PetNotFoundException::new);
        }
        else {
            savedPet = petRepository.save(toSavePet);
        }

        final Customer savedCustomer = savedPet.getOwner();
        savedCustomer.getPets().add(savedPet);
        customerRepository.save(savedCustomer);

        return petToPetDTOConverter.convert(savedPet);
    }

    private Pet convertPetDTOToPet(final PetDTO petDTO, final Customer customer) {
        final Pet pet = new Pet();

        if (petDTO.getId() > 0) {
            pet.setId(petDTO.getId());
        }

        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        pet.setOwner(customer);

        return pet;
    }

}
