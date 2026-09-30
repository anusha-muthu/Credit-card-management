package com.ofss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ofss.entity.UserPassword;

public interface userMerchantpasswordRepository
        extends JpaRepository<UserPassword, Long> {

    Optional<UserPassword> findByUserName(String userName);
}