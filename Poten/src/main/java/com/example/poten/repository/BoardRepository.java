package com.example.poten.repository;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUser(User user);
    List<Board> findAllByClub(Club club);
}
