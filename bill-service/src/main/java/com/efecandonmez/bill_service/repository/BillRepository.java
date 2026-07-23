package com.efecandonmez.bill_service.repository;

import com.efecandonmez.bill_service.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByAccountIdAndIsPaidFalse(Long accountId);

}