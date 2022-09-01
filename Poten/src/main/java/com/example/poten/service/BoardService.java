package com.example.poten.service;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.User;
import com.example.poten.dto.request.SaveBoardForm;
import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.UserRepository;
import javax.transaction.Transactional;
import jdk.jshell.spi.ExecutionControl.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository,  ClubRepository clubRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    /**
     * 게시물 업로드
     */
    public Board save(User loginUser, Club userClub, SaveBoardForm form){
        /**
         * 검증 0 : 해당 유저가 있는지 확인
         * 검증 1 : 해당 유저가 동아리 소속이 맞는지 확인
         */
        User findFromRepoUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new com.example.poten.exception.UserException("등록된 회원이 없습니다."));

        // 추가 검증 필요
//        Club findFromRepoClub = clubRepository.findById(userClub.getId()).orElseThrow(() -> new com.example.poten.exception.ClubException("해당 유저는 "));

//        clubRepository.findAllByMembers(loginUser);



        Board board = form.toBoard(loginUser, userClub);

        Board savedBoard = boardRepository.save(board);

//        findFromRepoClub.addBoard(savedBoard);
        return board;
    }
}
