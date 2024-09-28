package com.pkaczmarek.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookCreateDto(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Author cannot be blank")
        String author,
        @NotBlank(message = "ISBN cannot be blank")
        @Size(min = 13, max = 13, message = "ISBN must be 13 characters")
        String isbn
) {}
