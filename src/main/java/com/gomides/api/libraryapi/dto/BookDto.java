package com.gomides.api.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {


    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String autor;

    @NotNull
    private String isbn;
}
