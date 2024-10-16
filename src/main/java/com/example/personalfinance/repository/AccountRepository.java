package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAllByUser(User user);
    List<Account> findAllByUserAndIsDeletedFalse(User user);
    
}
