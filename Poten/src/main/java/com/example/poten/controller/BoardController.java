package com.example.poten.controller;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.Comment;
import com.example.poten.domain.HeartBoard;
import com.example.poten.domain.User;
import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.request.BoolResponse;
import com.example.poten.dto.request.CommentForm;
import com.example.poten.dto.request.HeartForm;
import com.example.poten.dto.response.BoardResponse;
import com.example.poten.dto.response.BoardResponseList;
import com.example.poten.dto.response.CommentResponse;
import com.example.poten.dto.response.CommentResponseList;
import com.example.poten.service.BoardService;
import com.example.poten.service.ClubService;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final ClubService clubService;

    public BoardController(BoardService boardService, UserService userService, ClubService clubService) {
        this.boardService = boardService;
        this.userService = userService;
        this.clubService = clubService;
    }

    private void logError(List<FieldError> errors) {
        log.error("Board Errors = {}", errors);
    }

    /**
     * 피드
     */
    @ApiOperation(value = "피드 생성")
    @PostMapping("/upload")
    public ResponseEntity<?> saveBoard(HttpServletRequest request, @Valid @RequestBody BoardForm boardForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Club userClub = clubService.findByClubId(boardForm.getClubId());
        Board savedBoard = boardService.saveBoard(loginUser, userClub, boardForm);
        return ResponseEntity.ok(savedBoard.toResponse());
    }

    @ApiOperation(value = "피드 하나 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(HttpServletRequest request, @PathVariable Long boardId) {
        User loginUser = userService.getLoginUser(request); // <<TODO>>  로그인한 사용자 불러오기

        Board findBoard = boardService.findBoardByBoardId(boardId);
        return ResponseEntity.ok(findBoard.toResponse());
    }

    @ApiOperation(value = "동아리별 피드 조회")
    @GetMapping("/club/{clubId}")
    public ResponseEntity<?> getBoardByClub(HttpServletRequest request, @PathVariable Long clubId)  throws LoginException {
        User loginUser = userService.getLoginUser(request);

        Club club = clubService.findByClubId(clubId);
        List<Board> boardEntityList = boardService.findBoardByClubId(club);

        // DTO로 변환
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }

    @ApiOperation(value = "내가 쓴 피드 모두 조회")
    @GetMapping("/mypage/boards")
    public ResponseEntity<?> getBoardByUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request); // <<TODO>>  로그인한 사용자 불러오기

        List<Board> boardEntityList = boardService.findBoardByMemberId(loginUser);

        // DTO로 변환
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(boardResponseList);

//        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }

    @ApiOperation(value = "피드 수정")
    @PutMapping("/update/{boardId}")
    public ResponseEntity<?> uploadBoard(HttpServletRequest request,  @PathVariable Long boardId, @Valid @RequestBody BoardForm boardForm, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request); // <<TODO>>  로그인한 사용자 불러오기
        Board updatedBoard = boardService.updateBoard(loginUser, boardId, boardForm);
        return ResponseEntity.ok(updatedBoard.toResponse());
    }

    @ApiOperation(value = "피드 삭제")
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity deleteBoard(HttpServletRequest request, @PathVariable Long boardId, @Valid @RequestBody BoardForm boardForm, BindingResult bindingResult) throws LoginException {

        User loginUser = userService.getLoginUser(request); // <<TODO>>  로그인한 사용자 불러오기
        boolean deleteResult =  boardService.deleteBoard(loginUser, boardId);
        return ResponseEntity.ok(new BoolResponse(deleteResult));
    }

    /**
     * 댓글
     */

}
