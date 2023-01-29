package com.example.uberbackend.repositories;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.RideInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c.tokens FROM Client c WHERE c.email = ?1")
    Double getTokensByEmail(String email);

    @Query("SELECT rI FROM RideInvite rI WHERE rI.emailTo = ?1 AND rI.rideInviteStatus = com.example.uberbackend.model.enums.RideInviteStatus.PENDING")
    List<RideInvite> findAllRideInvites(String userEmail);

    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    Optional<Client> findByEmail(String email);
}
