package dev.krzysztof.transactional.services.impl;

import dev.krzysztof.transactional.domain.Person;
import dev.krzysztof.transactional.domain.dto.PersonDto;
import dev.krzysztof.transactional.exception.NotFoundException;
import dev.krzysztof.transactional.repositories.PersonRepository;
import dev.krzysztof.transactional.services.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final ModelMapper modelMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<PersonDto> getAll() {
        return personRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<PersonDto> createNew(List<PersonDto> personsDto) {
        List<Person> personList = personsDto.stream()
                .map(this::convertDtoToEntity)
                .collect(Collectors.toList());
        return personRepository.saveAll(personList).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PersonDto createNew(PersonDto personDto) {
        return convertEntityToDto(personRepository.save(convertDtoToEntity(personDto)));
    }


    @Override
    @Transactional
    public PersonDto update(Map<String, Object> update, Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found person about id= " + id));
        return partial(update, person);
    }


    @Override
    @Transactional
    public PersonDto update(PersonDto personDto, Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.isPresent() ? convertEntityToDto(mapTo(personDto, person.get()))
                : convertEntityToDto(personRepository.save(convertDtoToEntity(personDto)));
    }

    @Override
    public PersonDto convertEntityToDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public Person convertDtoToEntity(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }

    private Person mapTo(PersonDto personDto, Person person) {
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setAge(personDto.getAge());
        return person;
    }

    private PersonDto partial(Map<String, Object> update, Person person) {

        update.keySet().forEach(key -> {
            switch (key) {
                case "firstName":
                    person.setFirstName((String) update.get(key));
                    break;
                case "lastName":
                    person.setLastName((String) update.get(key));
                    break;
                case "age":
                    person.setAge((int) update.get(key));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + key);
            }
        });
        return convertEntityToDto(person);
    }
}
