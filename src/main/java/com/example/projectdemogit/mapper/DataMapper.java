package com.example.projectdemogit.mapper;

import org.modelmapper.ModelMapper;

public class DataMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T> T toEntity(Object dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public static <T> T toDTO(Object object, Class<T> dtoClass) {
        return modelMapper.map(object, dtoClass);
    }
}
