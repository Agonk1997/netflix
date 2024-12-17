package com.example.netflix.infrastructure;

public interface Convert <TDto, TEntity>{
    TDto toDto (TEntity entity);
    TEntity toEntity(TDto Dto);
}
