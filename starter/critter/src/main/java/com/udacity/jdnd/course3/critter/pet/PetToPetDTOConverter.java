package com.udacity.jdnd.course3.critter.pet;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
final class PetToPetDTOConverter implements Converter<Pet, PetDTO> {

    @Override
    public PetDTO convert(final Pet pet) {
        final PetDTO petDTO = new PetDTO();

        if (pet.getId() != null) {
            petDTO.setId(pet.getId());
        }

        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());

        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }

        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());

        return petDTO;
    }

}
