package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
