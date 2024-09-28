package com.pkaczmarek.repository;

import com.pkaczmarek.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByIsbn(String isbn);
}