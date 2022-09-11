package com.example.poten.service;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.Comment;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.HeartBoard;
import com.example.poten.domain.User;
import com.example.poten.dto.request.CommentForm;
import com.example.poten.dto.request.BoardForm;
import com.example.poten.exception.BoardException;
import com.example.poten.exception.ClubException;
import com.example.poten.exception.CommentException;
import com.example.poten.exception.HeartException;
import com.example.poten.exception.UserException;
import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.CommentRepository;
import com.example.poten.repository.HeartBoardRepository;
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
    private final CommentRepository commentRepository;
    private final HeartBoardRepository heartBoardRepository;
    private final FileService fileService;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository,  ClubRepository clubRepository, CommentRepository commentRepository, HeartBoardRepository heartBoardRepository, FileService fileService) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.commentRepository = commentRepository;
        this.heartBoardRepository = heartBoardRepository;
        this.fileService = fileService;
    }

    /**
     * 피드 업로드
     */
    public Board saveBoard(User loginUser, Club userClub, BoardForm form) throws Exception {
        /**
         * 검증 0 : 해당 유저가 있는지 확인
         * 검증 1 : 해당 유저가 동아리 소속이 맞는지 확인
         */
        User findFromRepoUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        List<Club> findClubs = clubRepository.findAllByMembers(loginUser);
        if(! CollectionUtils.isEmpty(findClubs) && !findClubs.contains(userClub)) throw new ClubException("해당 유저는 동아리 소속이 아닙니다.");

        List<FileEntity> picsToFileEnity = fileService.parseFileInfo(form.getPics());    // FileEntity로 변환
        Board savedBoard = boardRepository.save(form.toBoard(findFromRepoUser, userClub, picsToFileEnity));

        return savedBoard;
    }

    /**
     * 피드 조회
     */
    // 피드 하나 조회 (by 피드id)
    public Board findBoardById(Long boardId){
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("피드 조회 오류"));
        return findBoard;
    }

    // 피드 모두 조회 (by 사용자Id) / 내가 쓴 글
    public List<Board> findBoardByMemberId(User loginUser){
       List<Board> findFromRepoBoard =  boardRepository.findAllByUser(loginUser);
       return findFromRepoBoard == null ? Collections.emptyList() : findFromRepoBoard;
    }

    // 피드 모두 조회 (by 동아리Id)
    public List<Board> findBoardByClubId(Club club){
        List<Board> findFromRepoBoard =  boardRepository.findAllByClub(club);
        return findFromRepoBoard == null ? Collections.emptyList() : findFromRepoBoard;
    }


    /**
     * 피드 수정
     */
    public Board updateBoard(User loginUser, Long boardId, BoardForm form) throws Exception {
        User findUserFromRepo = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUserFromRepo.getId();
        Board findBoardFromRepo = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        if (!findUserId.equals(findBoardFromRepo.getUser().getId()))  throw new BoardException("해당 유저는 피드 작성자가 아닙니다.");

        List<FileEntity> picsToFileEnity = fileService.parseFileInfo(form.getPics());    // FileEntity로 변환
        findBoardFromRepo.update(form, picsToFileEnity);
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
     * 댓글 달기
     */
    public Comment saveComment(User loginUser, Long boardId, CommentForm form){
        User findFromRepoUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        Board findBoardFromRepo = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        Comment comment  = Comment.builder()
            .user(findFromRepoUser)
            .board(findBoardFromRepo)
            .content(form.getContent())
            .build();

        commentRepository.save(comment);
        return comment;
    }

    /**
     *  피드 좋아요 누름
     */
    public HeartBoard heartBoard(User loginUser, Long BoardId) {
        Board findBoardFromRepo = boardRepository.findById(BoardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        // 이미 좋아요 누른 유저인지 확인
        if(heartBoardRepository.findHeartBoardByUserAndBoard(loginUser, findBoardFromRepo).isPresent()) throw new HeartException("이미 좋아요 누른 피드입니다.");

        HeartBoard heartBoard = HeartBoard.builder()
            .board(findBoardFromRepo)
            .user(loginUser)
            .build();

        heartBoardRepository.save(heartBoard);
        return heartBoard;
    }

    /**
     *  피드 좋아요 해제
     */
    public boolean unHeartBoard(User loginUser, Long BoardId) {
        Board findBoardFromRepo = boardRepository.findById(BoardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        HeartBoard heartBoard = heartBoardRepository.findHeartBoardByUserAndBoard(loginUser, findBoardFromRepo).orElseThrow(() -> new HeartException("좋아요 누르지 않은 피드입니다."));

        heartBoardRepository.delete(heartBoard);
        return true;
    }


    /**
     * 댓글 조회
     */
    // <<TODO>>  필요성 검토
    // 댓글 하나 조회 (by 댓글id)
    public Comment findCommentByCommentId(Long commentId){
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException("존재하지 않는 댓글입니다."));
        return findComment;
    }

    // 피드에 달린 모든 댓글 조회 (by boardId)
    public List<Comment> findCommentByBoardId(Long boardId){
        Board findBoardFromRepo = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("존재하지 않는 피드입니다."));

        List<Comment> findFromRepoComment = commentRepository.findAllByBoard(findBoardFromRepo);
        return findFromRepoComment == null ? Collections.emptyList() : findFromRepoComment;
    }

    // 댓글 모두 조회 (by 사용자Id) / 내가 쓴 댓글
    public List<Comment> findCommentByMember(User loginUser){
        List<Comment> findFromRepoComment =  commentRepository.findAllByUser(loginUser);
        return findFromRepoComment == null ? Collections.emptyList() : findFromRepoComment;
    }

    /**
     * 댓글 수정
     */
    public Comment updateComment(User loginUser, Long commentId, CommentForm form){
        User findFromRepoUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findFromRepoUser.getId();
        Comment findCommentFromRepo = commentRepository.findById(commentId).orElseThrow(() -> new CommentException("존재하지 않는 댓글입니다."));

        if(! findUserId.equals(findCommentFromRepo.getUser().getId())) throw new CommentException("해당 유저는 댓글 작성자가 아닙니다.");

        findCommentFromRepo.update(form);
        return findCommentFromRepo ;
    }

    /**
     * 댓글 삭제
     */
    public boolean deleteComment(User loginUser, Long commentId){
        User findFromRepoUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findFromRepoUser.getId();
        Comment findCommentFromRepo = commentRepository.findById(commentId).orElseThrow(() -> new CommentException("존재하지 않는 댓글입니다."));

        if(! findUserId.equals(findCommentFromRepo.getUser().getId())) throw new CommentException("해당 유저는 댓글 작성자가 아닙니다.");

        commentRepository.delete(findCommentFromRepo);
        return true;
    }

}
