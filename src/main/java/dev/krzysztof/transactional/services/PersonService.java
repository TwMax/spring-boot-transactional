package dev.krzysztof.transactional.services;

import dev.krzysztof.transactional.domain.Person;
import dev.krzysztof.transactional.domain.dto.PersonDto;

import java.util.List;
import java.util.Map;

public interface PersonService extends GenericService<PersonDto, Person> {

    List<PersonDto> getAll();

    List<PersonDto> createNew(List<PersonDto> personsDto);

    PersonDto createNew(PersonDto personDto);

    PersonDto update(Map<String, Object> update, Long id);

    PersonDto update(PersonDto personDto, Long id);

    PersonDto convertEntityToDto(Person person);

    Person convertDtoToEntity(PersonDto personDto);

}
