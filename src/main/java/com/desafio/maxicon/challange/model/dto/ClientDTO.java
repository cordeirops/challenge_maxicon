package com.desafio.maxicon.challange.model.dto;


import jakarta.validation.constraints.*;

public record ClientDTO(

        long id,
        @NotBlank
        String name,

        @Min(18)
        @NotNull
        Integer age,

        @Email
        String email,

        @NotNull
        String cpf
) {
}
