package dev.krzysztof.transactional.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.krzysztof.transactional.domain.dto.PersonDto;
import dev.krzysztof.transactional.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {
    List<PersonDto> personDtoList = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServiceImpl personServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        personDtoList.add(new PersonDto("Krzysztof", "Gawłowski", 51));
        personDtoList.add(new PersonDto("Adam", "Nowak", 49));
        personDtoList.add(new PersonDto("Jan", "Kowalski", 11));
    }

    @Test
    void shouldReturnAllPerson() throws Exception {
        //when
        when(personServiceImpl.getAll()).thenReturn(personDtoList);
        mockMvc.perform(get("/persons")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldCreatedNewPersons() throws Exception {
        //then
        when(personServiceImpl.createNew(anyList())).thenReturn(personDtoList);
        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDtoList)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void shouldCreatedNewPerson() throws Exception {
        // given
        PersonDto personDto = new PersonDto("Krzysztof", "Gawłowski", 51);

        // when
        when(personServiceImpl.createNew((PersonDto) any())).thenReturn(personDto);
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDto.getLastName())))
                .andExpect(jsonPath("$.age", is(personDto.getAge())))
                .andDo(print());
    }

    @Test
    void shouldUpdatedPerson() throws Exception {
        // given
        PersonDto personDto = new PersonDto("Krzysztof", "Gawłowski", 51);

        // when
        when(personServiceImpl.update((PersonDto) any(), eq(1L))).thenReturn(personDto);

        mockMvc.perform(put("/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDto.getLastName())))
                .andExpect(jsonPath("$.age", is(personDto.getAge())))
                .andDo(print());
    }

    @Test
    void shouldUpdatedNewPersonPartial() throws Exception {
        // given
        PersonDto personDto = new PersonDto("Krzysztof", "Gawłowski", 51);

        // when
        when(personServiceImpl.update(anyMap(), eq(1L))).thenReturn(personDto);

        mockMvc.perform(patch(("/person/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(personDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(personDto.getLastName())))
                .andExpect(jsonPath("$.age", is(personDto.getAge())))
                .andDo(print());
    }

}