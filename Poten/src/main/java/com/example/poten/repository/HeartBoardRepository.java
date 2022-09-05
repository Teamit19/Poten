package com.example.poten.repository;

import com.example.poten.domain.Board;
import com.example.poten.domain.HeartBoard;
import com.example.poten.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartBoardRepository extends JpaRepository<HeartBoard, Long> {
    Optional<HeartBoard> findHeartBoardByUserAndBoard(User user, Board board);
}
