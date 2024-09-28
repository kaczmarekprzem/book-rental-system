package com.pkaczmarek.repository;

import com.pkaczmarek.model.RentedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentedBookRepository extends JpaRepository<RentedBook, Long> {
    Optional<RentedBook> findByIsbn(String isbn);
}
