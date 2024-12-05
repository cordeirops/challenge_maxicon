package com.desafio.maxicon.challange.model.client;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DataCreateClient(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        long id,
        @NotNull
        String name,

        @NotNull
        Integer age,

        @Email
        String email,

        @NotNull
        String cpf
) {
}
