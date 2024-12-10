package com.desafio.maxicon.challange.repository;

import com.desafio.maxicon.challange.model.dto.InstallmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstallmentRepository extends JpaRepository <InstallmentDTO, Long>{
    @Modifying
    @Query("DELETE FROM InstallmentDTO i WHERE i.loan.id = :loanId")
    void deleteByLoanId(@Param("loanPriceId") long loanId);

}
