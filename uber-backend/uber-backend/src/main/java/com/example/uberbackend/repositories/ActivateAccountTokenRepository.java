package com.example.uberbackend.repositories;

import com.example.uberbackend.model.ActivateAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivateAccountTokenRepository extends JpaRepository<ActivateAccountToken, Long> {

    Optional<ActivateAccountToken> findByToken(String token);
}
