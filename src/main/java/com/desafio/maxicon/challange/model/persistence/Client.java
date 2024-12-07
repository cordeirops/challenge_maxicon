package com.desafio.maxicon.challange.model.persistence;

import com.desafio.maxicon.challange.model.dto.ClientDTO;
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
    private Integer age;
    private String email;
    private String cpf;

    public Client(ClientDTO data) {
        this.name = data.name();
        this.age = data.age();
        this.email = data.email();
        this.cpf = data.cpf();
    }

    public void updateClient(ClientDTO data) {
        if (data.name() != null){
            this.name = data.name();
        }
        if (data.age() != null){
            this.age = data.age();
        }
        if (data.email() != null){
            this.email = data.email();
        }
        if (data.cpf() != null){
            this.cpf = data.cpf();
        }
    }
}
