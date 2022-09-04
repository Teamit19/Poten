package com.example.poten.service;

import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class ClubService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public ClubService(BoardRepository boardRepository, UserRepository userRepository,  ClubRepository clubRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }
}
