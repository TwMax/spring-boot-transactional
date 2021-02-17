package dev.krzysztof.transactional.services;

import java.util.List;
import java.util.Map;

public interface GenericService<T, D> {
    // T - object DTO
    // D - entity

    List<T> getAll();

    List<T> createNew(List<T> objectDto);

    T createNew(T objectDto);

    T update(Map<String, Object> map, Long id);

    T update(T personDto, Long id);

    T convertEntityToDto(D entity);

    D convertDtoToEntity(T object);
}
