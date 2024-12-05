package com.desafio.maxicon.challange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "clients")
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int age;
    private String email;
    private String cpf;

    public Client(DataCreateClient data) {
        this.name = data.name();
        this.age = data.age();
        this.email = data.email();
        this.cpf = data.cpf();
    }
}
