package com.example.monitorconsumo.utils;


import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConversorEntidadeDTO<E, D> {
    private final ModelMapper modelMapper;
    
    private final Class<E> eClass;
    private final Class<D> dClass;
    
    public ConversorEntidadeDTO(Class<E> eClass, Class<D> dClass) {
        this.eClass = eClass;
        this.dClass = dClass;
        this.modelMapper = new ModelMapper();
    }
    
    public List<D> toDTO(final Collection<E> entityList) {
        return entityList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<E> toEntity(final Collection<D> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    
    public E toEntity(D d) {
        return modelMapper.map(d, eClass);
    }
    
    public D toDTO(E e) {
        return modelMapper.map(e, dClass);
    }
    
    
}
