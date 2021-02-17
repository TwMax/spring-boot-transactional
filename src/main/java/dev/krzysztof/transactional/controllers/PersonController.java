package dev.krzysztof.transactional.controllers;

import dev.krzysztof.transactional.domain.dto.PersonDto;
import dev.krzysztof.transactional.services.impl.PersonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceImpl personServiceImpl;

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> getAllPersons () {
        return new ResponseEntity<>(personServiceImpl.getAll(), HttpStatus.OK);
    }

    @PostMapping("/persons")
    public ResponseEntity<List<PersonDto>> createNewPersons(@RequestBody List<PersonDto> personsDto) {
        return new ResponseEntity<>(personServiceImpl.createNew(personsDto), HttpStatus.CREATED);
    }

    @PostMapping("/person")
    public ResponseEntity<PersonDto> createNewPerson(@RequestBody PersonDto personDto) {
        return new ResponseEntity<>(personServiceImpl.createNew(personDto), HttpStatus.CREATED);
    }

    @PatchMapping("/person/{id}")
    public ResponseEntity<PersonDto> updatePerson (@RequestBody Map<String, Object> update, @PathVariable("id") Long id) {
        PersonDto result = personServiceImpl.update(update, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/person/{id}")
    public ResponseEntity<PersonDto> updatePerson(@RequestBody PersonDto personDto, @PathVariable("id") Long id) {
        PersonDto result = personServiceImpl.update(personDto, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
