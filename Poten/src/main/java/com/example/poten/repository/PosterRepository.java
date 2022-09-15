package com.example.poten.repository;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.Poster;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Poster, Long> {
    List<Poster> findAllByClubOrderByDeadlineDate(Club club);

    List<Poster> findAllByOrderByDeadlineDate();

}
