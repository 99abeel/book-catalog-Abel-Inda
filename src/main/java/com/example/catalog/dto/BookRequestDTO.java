package com.example.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequestDTO(
        @NotBlank String isbn,
        @NotBlank String title,
        @NotBlank String author,
        @Min(0) int year
) {
}
