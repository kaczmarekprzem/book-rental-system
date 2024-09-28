package com.pkaczmarek.mapper;

import com.pkaczmarek.dto.RentedBookDto;
import com.pkaczmarek.model.RentedBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentedBookMapper {
    RentedBookDto toDto(RentedBook rentedBook);

    @Mapping(target = "id", ignore = true)
    RentedBook toEntity(RentedBookDto rentedBookDto);
}
