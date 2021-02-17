package dev.krzysztof.transactional.services;

import dev.krzysztof.transactional.domain.Person;
import dev.krzysztof.transactional.domain.dto.PersonDto;
import dev.krzysztof.transactional.exception.NotFoundException;
import dev.krzysztof.transactional.repositories.PersonRepository;
import dev.krzysztof.transactional.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    List<PersonDto> personDtoList = new ArrayList<>();

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Spy
    ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        personDtoList.add(new PersonDto("Krzysztof", "Gawłowski", 51));
        personDtoList.add(new PersonDto("Adam", "Nowak", 49));
        personDtoList.add(new PersonDto("Jan", "Kowalski", 11));
    }

    @Test
    void shouldReturnAllPersons() {
        //given

        List<Person> persons = new ArrayList<>();
        persons.add(modelMapper.map(new PersonDto("Krzysztof", "Gawłowski", 51), Person.class));
        persons.add(modelMapper.map(new PersonDto("Adam", "Nowak", 49), Person.class));
        persons.add(modelMapper.map(new PersonDto("Jan", "Kowalski", 11), Person.class));
        //when
        when(personRepository.findAll()).thenReturn(persons);
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        List<PersonDto> personDtoListResponse = personServiceImpl.getAll();

        //then
        assertAll(
                () -> assertEquals(personDtoListResponse.size(), personDtoList.size()),
                () -> verify(personRepository, times(1)).findAll()
        );
    }

    @Test
    void shouldReturnSavePersons() {
        //given
        List<Person> persons = new ArrayList<>();
        persons.add(modelMapper.map(new PersonDto("Krzysztof", "Gawłowski", 51), Person.class));
        persons.add(modelMapper.map(new PersonDto("Adam", "Nowak", 49), Person.class));
        persons.add(modelMapper.map(new PersonDto("Jan", "Kowalski", 11), Person.class));

        //when
        when(personRepository.saveAll(anyList())).thenReturn(persons);
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        List<PersonDto> personDtoListResponse = personServiceImpl.createNew(personDtoList);

        //then
        assertAll(
                () -> assertEquals(personDtoListResponse.size(), personDtoList.size()),
                () -> verify(personRepository, times(1)).saveAll(anyList())
        );
    }

    @Test
    void shouldReturnSavePerson() {
        //given
        PersonDto personDto = new PersonDto("Krzysztof", "Gawłowski", 51);
        Person person = modelMapper.map(personDto, Person.class);

        //when
        when(personRepository.save(any(Person.class))).thenReturn(person);
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        PersonDto personDtoResponse = personServiceImpl.createNew(personDto);

        //then
        assertAll(
                () -> assertEquals(person.getFirstName(), personDtoResponse.getFirstName()),
                () -> assertEquals(person.getLastName(), personDtoResponse.getLastName()),
                () -> assertEquals(person.getAge(), personDtoResponse.getAge()),
                () -> verify(personRepository, times(1)).save(any())
        );
    }

    @Test
    void shouldReturnUpdatePerson() {
        //given
        PersonDto personDto = new PersonDto("Krzysztof", "Gawłowski", 51);
        PersonDto personDtoRepo = new PersonDto("Adam", "Nowak", 12);

        Person person = Optional.of(modelMapper.map(personDtoRepo, Person.class)).get();

        //when
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        PersonDto personDtoResponse = personServiceImpl.update(personDto, 1L);

        //then
        assertAll(
                () -> assertEquals(personDto.getFirstName(), personDtoResponse.getFirstName()),
                () -> assertEquals(personDto.getLastName(), personDtoResponse.getLastName()),
                () -> assertEquals(personDto.getAge(), personDtoResponse.getAge()),
                () -> verify(personRepository, never()).save(any())
        );
    }

    @Test
    void shouldReturnUpdateParialPerson() {
        //given
        Map<String, Object> mapPersonDto = new HashMap<>();
        mapPersonDto.put("firstName", "Krzysztof");
        mapPersonDto.put("lastName", "Gawłowski");
        PersonDto personDtoRepo = new PersonDto("Adam", "Nowak", 12);

        Person person = Optional.of(modelMapper.map(personDtoRepo, Person.class)).get();

        //when
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        PersonDto personDtoResponse = personServiceImpl.update(mapPersonDto, 1L);

        //then
        assertAll(
                () -> assertEquals(mapPersonDto.get("firstName"), personDtoResponse.getFirstName()),
                () -> assertEquals(mapPersonDto.get("lastName"), personDtoResponse.getLastName()),
                () -> assertEquals(personDtoRepo.getAge(), personDtoResponse.getAge()),
                () -> verify(personRepository, never()).save(any())
        );
    }

    @Test
    void shouldReturnUpdatePersonException() {
        //given
        Map<String, Object> mapPersonDto = new HashMap<>();
        mapPersonDto.put("firstName", "Krzysztof");
        mapPersonDto.put("lastName", "Gawłowski");
        PersonDto personDtoRepo = new PersonDto("Adam", "Nowak", 12);

        //when
        when(personRepository.findById(2L)).thenReturn(Optional.empty());
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        assertThrows(NotFoundException.class, () -> personServiceImpl.update(mapPersonDto, 2L));
    }

    @Test
    void shouldReturnUpdateParialPersonException() {
        //given
        Map<String, Object> mapPersonDto = new HashMap<>();
        mapPersonDto.put("firstName", "Krzysztof");
        mapPersonDto.put("X", "Gawłowski");
        PersonDto personDtoRepo = new PersonDto("Adam", "Nowak", 12);

        Person person = Optional.of(modelMapper.map(personDtoRepo, Person.class)).get();

        //when
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        PersonServiceImpl personServiceImpl = new PersonServiceImpl(personRepository, modelMapper, applicationEventPublisher);
        assertThrows(IllegalStateException.class, () -> personServiceImpl.update(mapPersonDto, 1L));
    }
}