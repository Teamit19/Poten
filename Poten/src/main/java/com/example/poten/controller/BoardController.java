package com.example.poten.controller;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.Comment;
import com.example.poten.domain.HeartBoard;
import com.example.poten.domain.User;
import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.request.BoolResponse;
import com.example.poten.dto.request.CommentForm;
import com.example.poten.dto.response.*;
import com.example.poten.service.BoardService;
import com.example.poten.service.ClubService;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
     * ??????
     */
    @ApiOperation(value = "?????? ??????")
    @PostMapping("/upload")
    public ResponseEntity<?> saveBoard(HttpServletRequest request, @ModelAttribute @Valid BoardForm boardForm, BindingResult bindingResult)
        throws Exception {
        log.error("SAVEBOARD ????????? - content : " + boardForm.getContent());
//        System.out.println("test 2 ?????? pics" + pics);
        System.out.println("test 2 ?????? boardForm" + boardForm.getPics().toString());
        if (bindingResult.hasErrors()) {
            log.error("????????? ??????");
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }
        log.error("111" + boardForm.getClubId() + boardForm.getContent() );
        User loginUser = userService.getLoginUser(request) ;
        log.error("222");
        Club userClub = clubService.findByClubId(boardForm.getClubId());
        log.error("333");
        Board savedBoard = boardService.saveBoard(loginUser, userClub, boardForm);
        log.error("44444");
        return ResponseEntity.ok(savedBoard.toResponse());
//        return ResponseEntity.ok(savedBoard);
    }

    @ApiOperation(value = "?????? ?????? ??????")
    @GetMapping("/all")
    public ResponseEntity<?> getBoardAll(HttpServletRequest request) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        List<Board> boardEntityList = boardService.findBoardAll();

        // DTO??? ??????
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }


    @ApiOperation(value = "?????? ?????? ??????")
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(HttpServletRequest request, @PathVariable Long boardId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        Board findBoard = boardService.findBoardById(boardId);
        return ResponseEntity.ok(findBoard.toResponse());
    }

    @ApiOperation(value = "???????????? ?????? ??????")
    @GetMapping("/club/{clubId}")
    public ResponseEntity<?> getBoardByClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        Club club = clubService.findByClubId(clubId);
        List<Board> boardEntityList = boardService.findBoardByClubId(club);

        // DTO??? ??????
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }

    @ApiOperation(value = "???????????? ????????? ?????? ??????")
    @GetMapping("/follows")
    public ResponseEntity<?> getBoardByFollow(HttpServletRequest request) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        List<Club> clubList = clubService.findFollowingByUser(loginUser);
        List<Board> boardEntityList = boardService.findBoardByClubList(clubList);

        // DTO??? ??????
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }

    @ApiOperation(value = "?????? ?????? ??????")
    @GetMapping("/interest")
    public ResponseEntity<?> getBoardByInterest(HttpServletRequest request) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        List<Club> clubList = clubService.getInterestClub(loginUser);
        List<Board> boardEntityList = boardService.findBoardByClubList(clubList);

        // DTO??? ??????
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }

    @ApiOperation(value = "?????? ??? ?????? ?????? ??????")
    @GetMapping("/mypage")
    public ResponseEntity<?> getBoardByUser(HttpServletRequest request) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        List<Board> boardEntityList = boardService.findBoardByMemberId(loginUser);

        // DTO??? ??????
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardEntityList.forEach(b -> boardResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new BoardResponseList(boardResponseList));
    }

    @ApiOperation(value = "?????? ??????")
    @PutMapping("/update/{boardId}")
    public ResponseEntity<?> updateBoard(HttpServletRequest request,  @PathVariable Long boardId, @Valid @RequestBody BoardForm boardForm, BindingResult bindingResult)
        throws Exception {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Board updatedBoard = boardService.updateBoard(loginUser, boardId, boardForm);
        return ResponseEntity.ok(updatedBoard.toResponse());
    }

    @ApiOperation(value = "?????? ??????")
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity deleteBoard(HttpServletRequest request, @PathVariable Long boardId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        boolean deleteResult =  boardService.deleteBoard(loginUser, boardId);
        return ResponseEntity.ok(new BoolResponse(deleteResult));
    }

    @ApiOperation(value = "?????? ?????????")
    @PostMapping("/{boardId}/heart")
    public ResponseEntity heartBoard(HttpServletRequest request, @PathVariable Long boardId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        HeartBoard savedBoard =  boardService.heartBoard(loginUser, boardId);
        return ResponseEntity.ok(savedBoard.toResponse());
    }

    @ApiOperation(value = "?????? ????????? ??????")
    @PostMapping("/{boardId}/unheart")
    public ResponseEntity unHeartBoard(HttpServletRequest request, @PathVariable Long boardId, BindingResult bindingResult) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        boolean unHeartResult =  boardService.unHeartBoard(loginUser, boardId);
        return ResponseEntity.ok(new BoolResponse(unHeartResult));
    }


    /**
     * ??????
     */
    @ApiOperation(value = "?????? ??????")
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> saveComment(HttpServletRequest request, @PathVariable Long boardId, @Valid @RequestBody CommentForm commentForm, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);

        Comment savedcomment = boardService.saveComment(loginUser, boardId, commentForm);
        return ResponseEntity.ok(savedcomment.toResponse());
    }

//    // <<TODO>>  ????????? ??????
//    @ApiOperation(value = "?????? ?????? ??????")
//    @PostMapping("/comments/{commentId}")
//    public ResponseEntity<?> getComment(HttpServletRequest request, @PathVariable Long commentId) {
//        User loginUser = userService.getLoginUser(request);
//
//        Comment findComment = boardService.findCommentByCommentId(commentId);
//        return ResponseEntity.ok(findComment.toResponse());
//    }

    @ApiOperation(value = "????????? ????????? ??????")
    @GetMapping("/{boardId}/comments/")
    public ResponseEntity<?> getCommentByBoard(HttpServletRequest request, @PathVariable Long boardId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        List<Comment> findCommentList = boardService.findCommentByBoardId(boardId);

        // DTO??? ??????
        List<CommentResponse> commentResponseList = new ArrayList<>();
        findCommentList.forEach(c -> commentResponseList.add(c.toResponse()));

        return ResponseEntity.ok(new CommentResponseList(commentResponseList));
    }

    @ApiOperation(value = "?????? ??? ?????? ?????? ??????")
    @GetMapping("/mypage/comments")
    public ResponseEntity<?> getCommentByUser(HttpServletRequest request) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        List<Comment> findCommentList = boardService.findCommentByMember(loginUser);

        // DTO??? ??????
        List<CommentResponse> commentResponseList = new ArrayList<>();
        findCommentList.forEach(c -> commentResponseList.add(c.toResponse()));

        return ResponseEntity.ok(new CommentResponseList(commentResponseList));
    }

    @ApiOperation(value = "?????? ??????")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(HttpServletRequest request, @PathVariable Long commentId, @Valid @RequestBody CommentForm commentForm, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Comment updateComment = boardService.updateComment(loginUser, commentId, commentForm);
        return ResponseEntity.ok(updateComment.toResponse());
    }

    @ApiOperation(value = "?????? ??????")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity deleteComment(HttpServletRequest request, @PathVariable Long commentId, @Valid @RequestBody BoardForm boardForm, BindingResult bindingResult) throws LoginException {

        User loginUser = userService.getLoginUser(request);
        boolean deleteResult =  boardService.deleteComment(loginUser, commentId);
        return ResponseEntity.ok(new BoolResponse(deleteResult));
    }

}