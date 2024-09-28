package com.pkaczmarek.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_rented_book_isbn", columnList = "isbn")})
public class RentedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String borrower;
}