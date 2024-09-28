package com.pkaczmarek.dto;

public record RentedBookDto(Long id, String title, String author, String isbn, String borrower) {
}
