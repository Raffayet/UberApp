package com.example.uberbackend.repositories;
import com.example.uberbackend.model.RideInvite;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideInviteRepository extends JpaRepository<RideInvite, Long> {
    @Query("SELECT rI FROM RideInvite rI WHERE rI.emailTo = ?1 AND rI.rideInviteStatus = com.example.uberbackend.model.enums.RideInviteStatus.PENDING")
    List<RideInvite> findAllRideInvites(String userEmail);
}
