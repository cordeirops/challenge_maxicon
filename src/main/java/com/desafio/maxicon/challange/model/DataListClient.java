package com.desafio.maxicon.challange.model;

public record DataListClient(long id, String name, int age, String email, String cpf) {
    public DataListClient (Client client){
        this(client.getId(), client.getName(), client.getAge(), client.getEmail(), client.getCpf());
    }
}
