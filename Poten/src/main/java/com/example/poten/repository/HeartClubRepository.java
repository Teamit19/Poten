package com.example.poten.repository;

import com.example.poten.domain.Club;
import com.example.poten.domain.HeartClub;
import com.example.poten.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartClubRepository extends JpaRepository<HeartClub, Long> {
    Optional<HeartClub> findHeartClubByUserAndClub(User user, Club club);

}
