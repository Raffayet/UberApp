package com.example.uberbackend.repositories;

import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("select u.email from User u where not u.role.name = 'ADMIN'")
    List<String> getUserEmails();

    @Query("select u.email from User u where not u.role.name = 'ADMIN' and u.blocked = false ")
    List<String> getNotBlockedUserEmails();
}
