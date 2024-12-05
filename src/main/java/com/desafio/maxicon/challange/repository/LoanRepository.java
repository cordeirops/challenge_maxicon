package com.desafio.maxicon.challange.repository;

import com.desafio.maxicon.challange.model.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
