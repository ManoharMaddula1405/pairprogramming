package com.example.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private Integer id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Price cannot be null")
    private Integer price;
}
