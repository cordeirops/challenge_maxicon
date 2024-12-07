package com.desafio.maxicon.challange.repository;

import com.desafio.maxicon.challange.model.persistence.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <Client, Long> {
}
