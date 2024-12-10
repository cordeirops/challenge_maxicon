package com.desafio.maxicon.challange.repository;

import com.desafio.maxicon.challange.model.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


    List<Loan> findByClient_Id(long id);

    void deleteByClient_Id(long id);
}
