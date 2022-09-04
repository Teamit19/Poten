package com.example.poten.service;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.User;
import com.example.poten.dto.request.HeartDto;
import com.example.poten.dto.request.SaveBoardForm;
import com.example.poten.exception.BoardException;
import com.example.poten.exception.ClubException;
import com.example.poten.exception.UserException;
import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
     * 피드 업로드
     */
    public Board saveBoard(User loginUser, Club userClub, SaveBoardForm form){
        /**
         * 검증 0 : 해당 유저가 있는지 확인
         * 검증 1 : 해당 유저가 동아리 소속이 맞는지 확인
         */
        User findFromRepoUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        List<Club> findClubs = clubRepository.findAllByMembers(loginUser);
        if(! CollectionUtils.isEmpty(findClubs) && !findClubs.contains(userClub)) throw new ClubException("해당 유저는 동아리 소속이 아닙니다.");

        Board savedBoard = boardRepository.save(form.toBoard(findFromRepoUser, userClub));
        return savedBoard;
    }

    /**
     * 피드 조회
     */
    // 피드 하나 조회 (by 피드id)
    public Board findByBoardId(Long boardId){
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("피드 조회 오류"));
        return findBoard;
    }

    // 피드 모두 조회 (by 사용자Id) / 내가 쓴 글
    public List<Board> findByMemberId(User loginUser){
       List<Board> findFromRepoBoard =  boardRepository.findAllByUser(loginUser);
       return findFromRepoBoard == null ? Collections.emptyList() : findFromRepoBoard;
    }

    // 피드 모두 조회 (by 동아리Id)
    public List<Board> findByClubId(Club club){
        List<Board> findFromRepoBoard =  boardRepository.findAllByClub(club);
        return findFromRepoBoard == null ? Collections.emptyList() : findFromRepoBoard;
    }


    /**
     * 피드 수정
     */
    public Board updateBoard(User loginUser, Long boardId, SaveBoardForm form){
        User findUserFromRepo = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUserFromRepo.getId();
        Board findBoardFromRepo = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        if (!findUserId.equals(findBoardFromRepo.getUser().getId()))  throw new BoardException("해당 유저는 피드 작성자가 아닙니다.");

        findBoardFromRepo.update(form);
        return findBoardFromRepo;
    }


    /**
     * 피드 삭제
     */
    public boolean deleteBoard(User loginUser, Long boardId){
        User findUserFromRepo = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUserFromRepo.getId();
        Board findBoardFromRepo = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        if (!findUserId.equals(findBoardFromRepo.getUser().getId())) throw new BoardException("해당 유저는 피드 작성자가 아닙니다.");

        boardRepository.deleteById(boardId);
        return true;
    }

    /**
     * 좋아요 누르기
     */
    public boolean heartBoard(User loginUser, HeartDto heartDto){
        Board findBoardFromRepo = boardRepository.findById(heartDto.getTargetId()).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        // 이미 좋아요 누른 유저인지 확인
        if (boardRepository.findByIdAndHearts(findBoardFromRepo, loginUser).isPresent()) throw new BoardException("이미 좋아요 누른 피드입니다.");

        findBoardFromRepo.addLike(loginUser);
        return true;
    }

    /**
     * 좋아요 취소하기
     */
    public boolean unHeartBoard(User loginUser, HeartDto heartDto){
        Board findBoardFromRepo = boardRepository.findById(heartDto.getTargetId()).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        // 이미 좋아요 누른 유저인지 확인
        if (! boardRepository.findByIdAndHearts(findBoardFromRepo, loginUser).isPresent()) throw new BoardException("좋아요 하지 않은 피드입니다.");

//        findBoardFromRepo.updateLike(loginUser);
        return true;
    }

    /**
     * 댓글 달기
     */


}
