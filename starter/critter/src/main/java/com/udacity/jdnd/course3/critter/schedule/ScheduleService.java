package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetNotFoundException;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    private final EmployeeRepository employeeRepository;

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

        if (toSaveSchedule.getId() != null) {
            scheduleRepository.findById(toSaveSchedule.getId())
                    .orElseThrow(CustomerNotFoundException::new);
        }

        if (toSaveSchedule.getPets().stream()
                .anyMatch(pet -> !petRepository.findById(pet.getId()).isPresent())) {
            throw new PetNotFoundException();
        }

        if (toSaveSchedule.getEmployees().stream()
                .anyMatch(employee -> !employeeRepository.findById(employee.getId()).isPresent())) {
            throw new EmployeeNotFoundException();
        }

        final Schedule savedSchedule = scheduleRepository.save(toSaveSchedule);

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
