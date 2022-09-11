package com.example.poten.repository;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository  extends JpaRepository<Club, Long> {

    List<Club> findAllByMembers(User user);
    List<Club> findAllByNameContaining(String name);

}

