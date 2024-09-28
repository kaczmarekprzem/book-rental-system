package com.pkaczmarek.dto;

import jakarta.validation.constraints.NotBlank;

public record BookDto(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Author cannot be blank")
        String author,
        @NotBlank(message = "ISBN cannot be blank")
        String isbn,
        String category,
        String borrower
) {
}