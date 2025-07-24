package ru.meshgroup.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.meshgroup.persistance.model.Account;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a.id FROM Account a WHERE a.balance <= (a.initialDeposit * :multiplier) ORDER BY a.userId desc")
    List<Long> getAccountIdForIncrease(@Param("multiplier") BigDecimal multiplier);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Account getAccountByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    Account findByUserIdWithLock(@Param("userId") Long userId);

}
