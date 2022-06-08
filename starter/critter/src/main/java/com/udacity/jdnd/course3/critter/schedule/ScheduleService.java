package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final CustomerRepository customerRepository;

    private final ScheduleDTOToScheduleConverter scheduleDTOToScheduleConverter;

    private final ScheduleToScheduleDTOConverter scheduleToScheduleDTOConverter;

    public List<ScheduleDTO> findAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(scheduleToScheduleDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public ScheduleDTO save(final ScheduleDTO scheduleDTO) {
        final Schedule toSaveSchedule = scheduleDTOToScheduleConverter.convert(scheduleDTO);

        final Schedule savedSchedule;
        if (toSaveSchedule.getId() != null) {
            savedSchedule = scheduleRepository.findById(toSaveSchedule.getId())
                    .map(scheduleRepository::save)
                    .orElseThrow(ScheduleNotFoundException::new);
        }
        else {
            savedSchedule = scheduleRepository.save(toSaveSchedule);
        }

        return scheduleToScheduleDTOConverter.convert(savedSchedule);
    }

    public List<ScheduleDTO> findByPet(final long petId) {
        final Pet pet = new Pet();
        pet.setId(petId);

        return scheduleRepository.findAllByPets(pet)
                .stream()
                .map(scheduleToScheduleDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> findByEmployee(final long employeeId) {
        final Employee employee = new Employee();
        employee.setId(employeeId);

        return scheduleRepository.findAllByEmployees(employee)
                .stream()
                .map(scheduleToScheduleDTOConverter::convert)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> findByCustomer(final long customerId) {
        final Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);

        final List<ScheduleDTO> schedules = new ArrayList<>();

        final Set<Pet> pets = customer.getPets();
        if (pets != null) {
            for (final Pet pet : pets) {
                schedules.addAll(scheduleRepository.findAllByPets(pet).stream()
                        .map(scheduleToScheduleDTOConverter::convert)
                        .collect(Collectors.toList()));
            }
        }

        return schedules;
    }

}
