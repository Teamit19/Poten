package com.example.poten.service;

import com.example.poten.domain.Board;
import com.example.poten.domain.HeartBoard;
import com.example.poten.domain.User;
import com.example.poten.dto.request.HeartDto;
import com.example.poten.exception.BoardException;
import com.example.poten.exception.HeartException;
import com.example.poten.exception.UserException;
import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.HeartBoardRepository;
import com.example.poten.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class HeartBoardService {
    private final BoardRepository boardRepository;
    private final HeartBoardRepository heartBoardRepository;

    public HeartBoardService(BoardRepository boardRepository, HeartBoardRepository heartBoardRepository) {
        this.boardRepository = boardRepository;
        this.heartBoardRepository = heartBoardRepository;
    }

    /**
     *  피드 좋아요 누름
     */
    public void heartBoard(User loginUser, Long BoardId, HeartDto heartDto) {
        Board findBoardFromRepo = boardRepository.findById(heartDto.getTargetId()).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        // 이미 좋아요 누른 유저인지 확인
        if(heartBoardRepository.findHeartBoardByUserAndBoard(loginUser, findBoardFromRepo).isPresent()) throw new HeartException("이미 좋아요 누른 피드입니다.");

        HeartBoard heartBoard = HeartBoard.builder()
            .board(findBoardFromRepo)
            .user(loginUser)
            .build();

        heartBoardRepository.save(heartBoard);
    }

    /**
     *  피드 좋아요 해제
     */
    public void unHeartBoard(User loginUser, Long BoardId, HeartDto heartDto) {
        Board findBoardFromRepo = boardRepository.findById(heartDto.getTargetId()).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        HeartBoard heartBoard = heartBoardRepository.findHeartBoardByUserAndBoard(loginUser, findBoardFromRepo).orElseThrow(() -> new HeartException("좋아요 누르지 않은 피드입니다."));

        heartBoardRepository.delete(heartBoard);
    }

}
