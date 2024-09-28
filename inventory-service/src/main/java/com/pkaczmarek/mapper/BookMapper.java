package com.pkaczmarek.mapper;

import com.pkaczmarek.dto.BookCreateDto;
import com.pkaczmarek.dto.BookDto;
import com.pkaczmarek.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);
    Book toEntity(BookDto bookDto);
    Book toEntity(BookCreateDto bookDto);
}